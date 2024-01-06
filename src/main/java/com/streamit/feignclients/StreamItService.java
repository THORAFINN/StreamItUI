package com.streamit.feignclients;

import javax.servlet.http.HttpServletRequest;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import com.streamit.model.Dto.RegisterUserDto;
import com.streamit.model.Dto.ResponseDTO;


@FeignClient(name = "STREAMITSERVICE")
public interface StreamItService {
	
	
	@PostMapping(value = "/streamit/signup/new")
	public ResponseEntity<ResponseDTO> registerNewUser (@RequestBody  RegisterUserDto registerUserDto,@RequestHeader("Authorization") String authToken);
	

}
