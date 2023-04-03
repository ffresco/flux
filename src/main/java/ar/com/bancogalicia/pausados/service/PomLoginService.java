package ar.com.bancogalicia.pausados.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class PomLoginService {

    private static final Logger LOGGER = Logger.getLogger("PomLoginService.class");

    private static final String URL_JSON_HOLDER = "https://jsonplaceholder.typicode.com/todos/1";
    @Autowired
    RestTemplate restTemplate;

    public ResponseEntity<String> getTest() throws Exception {
        // Configrar la respuesta
        ResponseEntity<String> response = null;

        // Configuracion del Header
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        //headers.add("enviroment_key", SPREEDLY_ENVIROMENTAL_KEY);
        //request.setEnvironmentKey(SPREEDLY_ENVIROMENTAL_KEY);

        // Build the requestPost
        //HttpEntity<SpreedlyTokenizeRequest> requestPost = new HttpEntity<>(request, headers);

        try {
            response = restTemplate.getForEntity(URL_JSON_HOLDER, String.class);

        } catch (RestClientException e) {
            LOGGER.log(Level.SEVERE,"Error al invocar servicio " + e.getMessage());
            throw e;
        }

        return response;

    }


}
