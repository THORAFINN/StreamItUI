package com.streamit.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import io.netty.handler.codec.http.HttpMethod;

/**
 * StreamIt Ui Interceptor - It preHandle every request for our server and we allow only GET and POAT request else we return  
 * 
 */
@Component
public class StreamItUIInterceptor implements HandlerInterceptor{
	
	private static final Logger logger = LoggerFactory.getLogger(StreamItUIInterceptor.class);
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
	    String incomingMethod = request.getMethod();
	    logger.info("incomingMethod {}", incomingMethod);
	    if (incomingMethod.matches(HttpMethod.GET.toString()) || incomingMethod.matches(HttpMethod.POST.toString())) {
	    	 return true;
	    } else {
	       return false;	
	    }    
	}
}
