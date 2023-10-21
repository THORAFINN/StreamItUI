package com.streamit.common;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.json.JsonMapper;

@Configuration
public class JsonObjectMapper {
	
	@Bean
	public JsonMapper jsonMapper(){
		return JsonMapper.builder()
				.build();
				
	}
}
