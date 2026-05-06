package com.dawm.sonara;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class SonaraApplication {

	public static void main(String[] args) {
		SpringApplication.run(SonaraApplication.class, args);

	}
	//Cliente HTTP para hacer consultas a internet
	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

}
