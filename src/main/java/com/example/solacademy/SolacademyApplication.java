package com.example.solacademy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.example.solacademy.config.OracleWalletBootstrap;

@SpringBootApplication
public class SolacademyApplication {

	public static void main(String[] args) {
		OracleWalletBootstrap.ensureWalletExtractedToTempIfNeeded();
		SpringApplication.run(SolacademyApplication.class, args);
	}

}
