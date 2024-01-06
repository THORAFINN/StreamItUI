package com.streamit.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 
 * Spring MVC Configurer - we  add our StreamitUIInterceptor and add it to map all paths so that only GET and POST method are allowed.
 * <p>
 */
@Configuration
public  class StreamItUIInterceptorAppConfig implements WebMvcConfigurer{
	
	@Autowired
	private StreamItUIInterceptor StreamItUIInterceptor;
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(StreamItUIInterceptor).addPathPatterns("/**");
	}

}
