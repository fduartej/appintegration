package com.contoso.appintegration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.integration.config.EnableIntegration;

@SpringBootApplication
@EnableIntegration
public class AppintegrationApplication {

	public static void main(String[] args) {
		SpringApplication.run(AppintegrationApplication.class, args);
	}

}
