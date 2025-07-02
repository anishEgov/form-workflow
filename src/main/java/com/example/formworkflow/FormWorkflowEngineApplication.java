package com.example.formworkflow;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.validation.annotation.Validated;

@SpringBootApplication
@Validated
public class FormWorkflowEngineApplication {

	public static void main(String[] args) {
		SpringApplication.run(FormWorkflowEngineApplication.class, args);
	}

}
