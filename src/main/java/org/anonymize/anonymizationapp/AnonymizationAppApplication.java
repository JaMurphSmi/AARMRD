package org.anonymize.anonymizationapp;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration;


@SpringBootApplication
public class AnonymizationAppApplication {
    
	public static void main(String[] args) {
		SpringApplication.run(AnonymizationAppApplication.class, args);
	}

}
