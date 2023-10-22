package com.streamit.controller;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.streamit.model.ErrorConstant;
import com.streamit.model.StreamItConstant;
import com.streamit.model.Dto.LoginUserDto;
import com.streamit.model.Dto.RegisterUserDto;
import com.streamit.model.Dto.ResponseDTO;
import com.streamit.model.Dto.UploadVideoDto;
import com.streamit.model.Dto.VideoListDto;
import com.streamit.service.LoginAndSignUpService;
import com.streamit.service.VideoService;

import reactor.core.publisher.Mono;


@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/streamitui")
public class StreamItController {
	
	@Autowired
	private LoginAndSignUpService loginAndSignUpService; 
   
	@Autowired 
	private VideoService videoService;
	
	private Logger logger = LoggerFactory.getLogger(StreamItController.class);
	
	@PostMapping(StreamItConstant.SIGN_UP_NEW_USER)
	public ResponseEntity<ResponseDTO> registerNewUser (@RequestBody RegisterUserDto registerUserDto ){
		logger.info("StreamIt register New User api starts");
		logger.debug("request Body {}", registerUserDto);
		ResponseDTO responseDto = loginAndSignUpService.registerNewUser(registerUserDto);
		logger.debug("response from service {}", responseDto);
		logger.info("StreamIt register New User api ends");
		return new ResponseEntity<>(responseDto,HttpStatus.OK);
	}
	
	@PostMapping(StreamItConstant.LOGIN)
	public ResponseEntity<String> loginUserName (@RequestBody LoginUserDto loginUserDto ){
		logger.info("StreamIt login user api starts");
		logger.debug("request body {}", loginUserDto);
		String loginPassword = loginAndSignUpService.loginUser(loginUserDto);
		logger.info("StreamIt login user api ends ..");
		logger.debug("Response from service {}", loginPassword);
		if (loginPassword == null) {
			return new ResponseEntity<>(ErrorConstant.ERRMSG_INVALID_CREDITIALS,HttpStatus.UNAUTHORIZED); 
		} else {
			return new ResponseEntity<>(loginPassword,HttpStatus.OK);
		}
	}    
	
	
	@GetMapping( value= StreamItConstant.GET_VIDEO, produces = "video/mp4")
	public ResponseEntity<Mono<Resource>>  getVideo (@PathVariable String ref, HttpServletRequest request)  {
		logger.info("Get video By ref starts");
		logger.debug("Video ref {}", ref);
		String authTokken = request.getHeader(StreamItConstant.AUTHORIZATION);
		Integer userId = loginAndSignUpService.isValidUSer(authTokken);
		if (userId  == null) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}
		Mono<Resource> stream = videoService.getVideoByRef(ref);
	    if (stream == null) {
		   return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	   } else {
		   return new ResponseEntity<>(stream, HttpStatus.OK);
	   }
	}
	
	
	@PostMapping( value= StreamItConstant.UPLOAD_VIDEO, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<ResponseDTO> uploadFileApi (@RequestParam Map<String, String > searchParam ,
			                                         @RequestParam ("thumbnail") MultipartFile thumbNail,
													 @ModelAttribute UploadVideoDto dto,
			                                         HttpServletRequest request) throws IOException {
		logger.info("Streamit Upload video api start");
		logger.debug ("searchParams {}", searchParam);
		String authTokken = request.getHeader(StreamItConstant.AUTHORIZATION);
		Integer userId = loginAndSignUpService.isValidUSer(authTokken);
		if (userId  == null) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}
		if (searchParam.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		dto.setVideoTitle(searchParam.get("video_name"));
		dto.setThumbNailTitle(searchParam.get("thumbnail_title"));
		dto.setThumbNail(thumbNail);
		dto.setUserId(userId);
		ResponseDTO response = videoService.uploadVideoFile(dto);
		logger.info("Streamit Upload video api end");
		return new ResponseEntity<>(response, HttpStatus.OK); 
	}

	/**
	 * 
	 * <p>
	 * @param searchParam
	 * @param request
	 * @return
	 */
	@GetMapping(StreamItConstant.GET_VIDEO_LIST)
    public ResponseEntity<Page<VideoListDto>> getVideoList (@RequestParam Map<String, String> searchParam, HttpServletRequest request) {
    	logger.info("Get video List api start");
    	logger.debug ("searchParams {}", searchParam);
		String authTokken = request.getHeader(StreamItConstant.AUTHORIZATION);
		Integer userId = loginAndSignUpService.isValidUSer(authTokken);
		if (userId  == null) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}
		String stringPageIndex = searchParam.remove("pageIndex");
		String stringPageSize = searchParam.remove("pageSize");
		int pageIndex = Integer.parseInt(stringPageIndex);
		int pageSize = Integer.parseInt(stringPageSize);
		Sort sort = Sort.by(Direction.DESC, "vCrdDt");
		Pageable pageable = PageRequest.of(pageIndex, pageSize, sort);
		Page<VideoListDto> pageData = videoService.getVideoList(searchParam, pageable);
    	logger.info("get video List api end");
    	logger.debug("response from service {}", pageData);
    	return new ResponseEntity<>(pageData, HttpStatus.OK);
    }
	
}
