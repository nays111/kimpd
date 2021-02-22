package com.clnine.kimpd;

import com.baroservice.api.BarobillApiProfile;
import com.baroservice.api.BarobillApiService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.web.config.PageableHandlerMethodArgumentResolverCustomizer;

import java.net.MalformedURLException;

@SpringBootApplication
public class KimpdApplication {

	public static void main(String[] args) {
		SpringApplication.run(KimpdApplication.class, args);
	}

	@Bean
	public BarobillApiService barobillApiService() throws MalformedURLException {
		return new BarobillApiService(BarobillApiProfile.RELEASE);
	}
}
