package com.example.solacademy;

import java.io.File;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SolacademyApplication {

	public static void main(String[] args) {
		SpringApplication.run(SolacademyApplication.class, args);
	    String basePath = new File("").getAbsolutePath();
	    System.out.println(basePath);

	    String path = new File("src/main/resources/application.properties").getAbsolutePath();
	    System.out.println(path);
	}

}
