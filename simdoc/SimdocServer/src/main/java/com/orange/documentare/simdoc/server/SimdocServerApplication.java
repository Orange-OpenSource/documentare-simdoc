package com.orange.documentare.simdoc.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan
public class SimdocServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(SimdocServerApplication.class, args);
	}
}
