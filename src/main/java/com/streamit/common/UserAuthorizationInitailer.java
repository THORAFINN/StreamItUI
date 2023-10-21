package com.streamit.common;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.streamit.model.Db.Login;
import com.streamit.repository.LoginRepository;

@Component
public class UserAuthorizationInitailer implements CommandLineRunner{

	@Autowired
	private LoginRepository loginRepo;
	
	@Autowired
	private CryptoUtil cryptoUtil;
	
	private static Logger logger = LoggerFactory.getLogger(UserAuthorizationInitailer.class);
	
	public static Map<String,Integer> userAuthorization = new HashMap<>();

	
	public void generateMap () {
		logger.info("refresh source maps");
		List<Login> userList = loginRepo.findAll();
		userAuthorization.clear(); 
		
		for (Login login : userList) {
			String username = login.getUsername();
			String password = login.getPassword();
			Integer id = login.getId();
			String dataToEncode = username+"~"+password;
			logger.info("Data to encode {}", dataToEncode);
			String userPassword = cryptoUtil.Base64Encode(dataToEncode);
			logger.info("Encoded Password {}", userPassword);
			userAuthorization.put(userPassword, id);
		}
		
	}


	@Override
	public void run(String... args) throws Exception {
		generateMap();
		
	}
	
	
	
	
}
