package com.example.solacademy;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import com.example.solacademy.config.OracleWalletBootstrap;

public class ServletInitializer extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		OracleWalletBootstrap.ensureWalletExtractedToTempIfNeeded();
		return application.sources(SolacademyApplication.class);
	}

}
