package org.anonymize.anonymizationapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;


@SpringBootApplication
public class AnonymizationAppApplication {
    
	public static void main(String[] args) {
		SpringApplication.run(AnonymizationAppApplication.class, args);
	}
}
