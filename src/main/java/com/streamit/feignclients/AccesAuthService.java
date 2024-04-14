package com.streamit.feignclients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import com.streamit.model.Db.Login;
import com.streamit.model.Dto.AuthRequest;


@FeignClient(name = "AccessAuthService")
public interface AccesAuthService {
	
	@PostMapping("/access/validate/token")
	public ResponseEntity<Login> validateToken (@RequestHeader ("Authorization") String authTokken);
	
	@PostMapping("/access/token/generate")
	public String generateJwtToken (@RequestBody AuthRequest authrequest) throws Exception;
	
	@PostMapping("/access/token/refresh")
	public ResponseEntity<String> refreshToken (@RequestHeader("Authorization") String authTokken);

}
