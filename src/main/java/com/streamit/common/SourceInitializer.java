package com.streamit.common;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.streamit.model.Db.SrchHdr;
import com.streamit.repository.SrcHdrRepository;


@Component
public class SourceInitializer  implements CommandLineRunner{

	
	@Autowired
	private SrcHdrRepository srchHdrRepo;
	
	@Autowired
	private CryptoUtil cryptoUtil;
	
	private static final Logger logger = LoggerFactory.getLogger(SourceInitializer.class);
	
	
	public static Map<String, String> srcHdrMap = new HashMap<>();
	
	private void generateMaps () throws JsonProcessingException {
		List<SrchHdr> dataList = this.srchHdrRepo.findAll();
		for (SrchHdr srcHdr : dataList) {
			String token = srcHdr.getSrAppid()+"~" +srcHdr.getSrKey();
			String encryptToken = cryptoUtil.Base64Encode(token);
			srcHdrMap.put( srcHdr.getSrcName(),encryptToken);
			logger.debug(srcHdr.getSrcName()+" = "+encryptToken);
		}
	}
	
	
	@Override
	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub
		generateMaps();
	}

}
