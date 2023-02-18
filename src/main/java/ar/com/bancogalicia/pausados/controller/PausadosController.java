package ar.com.bancogalicia.pausados.controller;

import ar.com.bancogalicia.pausados.service.PausadosService;
import ar.com.bancogalicia.pausados.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RestController
@RequestMapping("/batch")
public class PausadosController {

    @Autowired
    PausadosService pausadosService;

    @Autowired
    @Qualifier(value = "storageService")
    StorageService storageService;

    @GetMapping("/")
    public ResponseEntity<String> getAll(){
        return ResponseEntity.status(HttpStatus.OK).body("Lista de Archivos procesados");

    }

    @PostMapping("/save")
    public String handleFileUpload(@RequestParam("file") MultipartFile file,
                                   RedirectAttributes redirectAttributes) {

        storageService.store(file);
        redirectAttributes.addFlashAttribute("message",
                "You successfully uploaded " + file.getOriginalFilename() + "!");

        return "redirect:/";
    }

    @PostMapping("/process_file")
    public ResponseEntity<String> process(@RequestBody String file){
        //Service
        Integer id = pausadosService.procesarCSV();
        return ResponseEntity.status(HttpStatus.OK).body("Archivo en proceso id: "+id);
    }

    @PostMapping("/process_batch")
    public ResponseEntity<String> prcessBatch(){
        //procesar batch
        Long id = pausadosService.processBatch();
        return ResponseEntity.status(HttpStatus.OK).body("Batch en proceso id: "+id);
    }

}
