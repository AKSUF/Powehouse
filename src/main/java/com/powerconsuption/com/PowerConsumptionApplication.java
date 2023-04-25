package com.powerconsuption.com;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableMBeanExport;

@SpringBootApplication
@EnableMBeanExport
public class PowerConsumptionApplication {

	
	public static void main(String[] args) {
		SpringApplication.run(PowerConsumptionApplication.class, args);
	}
	
//	The code defines a Spring bean for creating a new instance of ModelMapper, a Java library for mapping between objects.
	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}
	
	
	
}
