package ar.com.bancogalicia.pausados;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class GaliciaPausadosApplication {

	public static void main(String[] args) {
		SpringApplication.run(GaliciaPausadosApplication.class, args);
	}

}
