package com.streamit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients("com.streamit.feignclients")
@EnableAutoConfiguration(exclude = {SecurityAutoConfiguration.class})
public class StreamItApplication {

	public static void main(String[] args) {
		SpringApplication.run(StreamItApplication.class, args);
	}
	
	@Bean
	public RestTemplate getRestTesmplate () {
		return new  RestTemplateBuilder().build();
	}

}
