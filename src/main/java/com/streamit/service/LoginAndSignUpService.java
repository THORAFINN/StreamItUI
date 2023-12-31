package com.streamit.service;


import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.streamit.common.CryptoUtil;
import com.streamit.common.SourceInitializer;
import com.streamit.common.UserAuthorizationInitailer;
import com.streamit.feignclients.StreamItService;
import com.streamit.model.ErrorConstant;
import com.streamit.model.StreamItConstant;
import com.streamit.model.Dto.LoginUserDto;
import com.streamit.model.Dto.RegisterUserDto;
import com.streamit.model.Dto.ResponseDTO;
import com.streamit.repository.LoginRepository;

@Service
public class LoginAndSignUpService {
	
	@Autowired
	private LoginRepository  loginRepo;
	
	@Autowired
	private CryptoUtil cryptoUtil;
	
	@Autowired
	private StreamItService streamitService; 
	
	@Value("${streamit.service.login.url}")
	private String streamitServiceLoginUrl;
	
	private Logger logger = LoggerFactory.getLogger(LoginAndSignUpService.class); 

	public ResponseDTO registerNewUser (RegisterUserDto dto, HttpServletRequest request) {
		
		if ( dto.getName() == null ||  dto.getName().length() <= 0) {
		  return new ResponseDTO(2, ErrorConstant.ERROR_CODE_INVALID_NAME_VALUE, ErrorConstant.ERRMSG_INVALID_NAME_VALUE); 	
		} 
		
		if ( dto.getPassword() == null || dto.getPassword().length() <= 0) {
			return new ResponseDTO(2, ErrorConstant.ERROR_CODE_INVALID_PASSWORD_VALUE, ErrorConstant.ERRMSG_INVALID_PASSWORD_VALUE);
		} 
		
		if (dto.getUserName() == null || dto.getUserName().length() <= 0) {
			return new ResponseDTO(2, ErrorConstant.ERROR_CODE_INVALID_USERNAME_VALUE, ErrorConstant.ERRMSG_INVALID_USERNAME_VALUE);
		}
		
		if (dto.getEmail() == null || dto.getUserName().length() <= 0) {
			return new  ResponseDTO(2, ErrorConstant.ERROR_CODE_INVALID_USEREMAIL_VALUE, ErrorConstant.ERRMSG_INVALID_USEREMAIL_VALUE);
		}
		
		String authtoken = SourceInitializer.srcHdrMap.get(StreamItConstant.STREAMIT_UI);
		
		try {
			ResponseEntity<ResponseDTO> response = streamitService.registerNewUser(dto, authtoken);
			return  response.getBody();
		} catch (Exception e) {
	        logger.info("error occur when calling streamit service");
			logger.error(e.getMessage(),e);
			return new ResponseDTO(2, ErrorConstant.ERROR_CODE_INTERNAL_SERVER_ERROR, ErrorConstant.ERRMSG_INTERNAL_SERVER_ERROR);
		}
	} 
	
	
	public String loginUser (LoginUserDto dto) {
		if (loginRepo.existsByUsernameAndPassword(dto.getUserName(), dto.getPassword())){
			String dataToEncode = dto.getUserName()+"~"+dto.getPassword();
			logger.debug("Data to encode {}", dataToEncode);
			String userPassword = cryptoUtil.Base64Encode(dataToEncode);
			return userPassword;
		} else {
			return null;
		}
	}
	
	public Integer isValidUSer (String authTokken) {
		if (authTokken == null || authTokken.length() <= 0) {
			return null;
		}
		return UserAuthorizationInitailer.userAuthorization.get(authTokken);
		
	}
	
	
}
