package ar.com.bancogalicia.pausados.service;


import ar.com.bancogalicia.pausados.model.Batch;
import ar.com.bancogalicia.pausados.model.InvocationWSResult;
import ar.com.bancogalicia.pausados.model.Tarjeta;
import ar.com.bancogalicia.pausados.repository.BatchRepository;
import ar.com.bancogalicia.pausados.repository.InvocationWSResultRepository;
import ar.com.bancogalicia.pausados.repository.TarjetaRepository;
import com.opencsv.bean.CsvToBeanBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.io.File;
import java.io.FileReader;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class PausadosService {

    @Autowired
    BatchRepository batchRepository;

    @Autowired
    TarjetaRepository tarjetaRepository;

    @Autowired
    InvocationWSResultRepository invocationWSResultRepository;

    @Autowired
    @Qualifier(value = "storageService")
    StorageService storageService;


    private static Logger LOGGER = LogManager.getLogger(PausadosService.class);

    private static final String PROCESO_BATCH = "PROCESO DE PAUSADOS BATCH";
    private static final String BATCH_COMIENZO = "COMIENZO";
    private static final String BATCH_FIN_EXITO = "FIN_EXITO";
    private static final String BATCH_FIN_ERRORES = "FIN_ERRORES";

    private static final String WS_INVOCACION = "INVOCANDO WS";
    private static final String REG_EX_4XX = "(4[0-9][0-9])";

    private static final Integer SIN_PROCESAR = 0;
    private static final Integer PROCESADA = 2;

    public Integer procesarCSV() {
        try {
            File file = storageService.loadAsResource("tarjetas.csv").getFile();
            List<Tarjeta> beans = new CsvToBeanBuilder(new FileReader(file))
                    .withType(Tarjeta.class)
                    .build()
                    .parse();

            beans.forEach(System.out::println);
            Integer registrosProcesados = tarjetaRepository.saveAll(beans).size();
            return registrosProcesados;

        } catch (Exception e) {
            LOGGER.error(e);
        }
        return -1;
    }

    public Long processBatch() {
        Batch batch = null;
        try {
            batch = batchRepository.save(new Batch(null,PROCESO_BATCH, BATCH_COMIENZO,null,null,null)); //Se abre un proceso batch
            //List<Integer> listaPausados = getPausadasToProcess(); //se lee el lote a procesar de archivos csv
            List<Tarjeta> listaPausdos = tarjetaRepository.findByProcesada(0);

            boolean generoError = false;
            for (Tarjeta tarjeta : listaPausdos) {
                Long idTarjetaProcesada = null;
                Object modoCardResponse = null;
                int tarjetaProcesadaConExito = 0;
                InvocationWSResult invocationWSResult = new InvocationWSResult(null,null, WS_INVOCACION,tarjeta,batch);
                //Invocando al servicio
                try {
                    LOGGER.info("Invocando Servicio pausado 1 tarjeta PK" + tarjeta + " Tarjeta num : " + tarjeta);
                    //Se invoca al servicio simulo aca la llamada
                    String resultadoInvocacion = "200 OK";
                    invocationWSResult.setResultado(resultadoInvocacion);
                    invocationWSResultRepository.save(invocationWSResult);

                    //tarjetaProcesadaConExito = 1;
                    tarjeta.setProcesada(1);

                } catch (HttpClientErrorException e) {
                    if (e.getStatusCode() != null && Pattern.compile(REG_EX_4XX).matcher(e.getStatusCode().toString()).find()) {
                        LOGGER.debug("ERROR STATUS CODE 4XX, se saca tarjeta de la lista de batch a procesar para analizar falla");
                        //tarjetaProcesadaConExito = 2; //el dos significa que no se reprocesa pero hay que anailizar y volver a 0 cuando se resuelva
                        tarjeta.setProcesada(2);
                    }
                    tarjetaRepository.save(tarjeta);
                    generoError = true;

                } catch (Exception e) {
                    LOGGER.error("Se produjo un error al procesar la Tarjeta PK " + tarjeta + " Tarjeta num : " + tarjeta, e);
                    tarjetaRepository.save(tarjeta); //se graba en tarjeta procesada el error
                    generoError = true;
                    tarjetaProcesadaConExito = 0; //se deja sin procesar la tarjeta
                }
                // incremento el numero de veces que se proceso la tarjeta
                //usar esta logica (tarjeta.getId(), modoCardResponse != null ? modoCardResponse.getCardId() : null,tarjetaProcesadaConExito, tarjeta.getCantidadProcesos().intValue() + 1);
                tarjetaRepository.save(tarjeta);

            }
            if (generoError) {
                batch.setEstado(BATCH_FIN_ERRORES);
            } else {
                batch.setEstado(BATCH_FIN_EXITO);
            }
        } catch (Exception e) {
            LOGGER.error("Se produjo un error al procesar las tarjetas");
            if(batch!=null)
                batch.setEstado(BATCH_FIN_ERRORES);
        }
        batchRepository.save(batch);
        return batch.getId();
    }

    private List<Integer> getPausadasToProcess() {
        return Arrays.asList(1, 2, 3, 0, 4, 5);
    }

}
