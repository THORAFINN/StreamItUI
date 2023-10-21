package com.streamit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
//@EnableEurekaClient
public class StreamItApplication {

	public static void main(String[] args) {
		SpringApplication.run(StreamItApplication.class, args);
	}
	
	@Bean
	public RestTemplate getRestTesmplate () {
		return new  RestTemplateBuilder().build();
	}

}
