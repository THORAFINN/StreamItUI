package com.streamit.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.streamit.common.SourceInitializer;
import com.streamit.model.ErrorConstant;
import com.streamit.model.StreamItConstant;
import com.streamit.model.Db.Video;
import com.streamit.model.Dto.FileUploadReponseDto;
import com.streamit.model.Dto.ProcessFileDto;
import com.streamit.model.Dto.ResponseDTO;
import com.streamit.model.Dto.UploadVideoDto;
import com.streamit.model.Dto.UploadVideoandThumbNailDto;
import com.streamit.model.Dto.VideoListDto;
import com.streamit.repository.VideoRepository;

import reactor.core.publisher.Mono;


/**
 * 
 */
@Service
public class VideoService {
	
	@Autowired
	private VideoRepository videoRepo;
	
	@Autowired
	private RestTemplate restTemplate;;
	
	@Value("${streamit.service.uploadfile.url}")
	private String streamServiceUploadFileUrl;
	
	@Value("${streamit.service.processfile.url}")
	private String streamServiceProcessFileUrl;
	
	private static Logger logger = LoggerFactory.getLogger(VideoService.class);

	public ResponseDTO uploadVideoFile (UploadVideoDto dto) throws IOException {
		
		ResponseDTO validateResponse = validateRequest (dto);
		if (validateResponse != null) {
			return validateResponse;
		}
		
		FileUploadReponseDto fileUploadResponse = callUploadFileApi(dto);
		
		if (fileUploadResponse.getCode() == 1) {
			String videoFilePath = fileUploadResponse.getVideoFilePath();
			String videoFileName = fileUploadResponse.getVideoFileName();
			String thumnNailFilePath = fileUploadResponse.getThumnNailFilePath();
			String thumbNailFileName = fileUploadResponse.getThumbNailFileName();
			
			ProcessFileDto processFileRequestDto = new ProcessFileDto(dto.getVideoTitle(), 
																	  dto.getThumbNailTitle(), 
																	  videoFilePath, 
																	  videoFileName, 
																	  thumnNailFilePath, 
																	  thumbNailFileName, 
																	  dto.getUserId());
			
			ResponseDTO responseDto = callProcessFileApi(processFileRequestDto);
			return responseDto;
		}
		
		return new  ResponseDTO(2, fileUploadResponse.getErrorCode(), fileUploadResponse.getErrorMessage()) ;
	}
	
	
	/**
	 * 
	 * <p>
	 * @param dto
	 * @return
	 */
	private ResponseDTO callProcessFileApi (ProcessFileDto dto) {
		
		String authToken = SourceInitializer.srcHdrMap.get(StreamItConstant.STREAMIT_UI);
		
		HttpHeaders httpHeaders = new HttpHeaders();		
		httpHeaders.set(StreamItConstant.AUTHORIZATION, authToken);
		
		HttpEntity<Object> requestEntity = new HttpEntity<>(dto, httpHeaders);
		
		try {
			ResponseEntity<ResponseDTO> response = this.restTemplate.postForEntity(streamServiceProcessFileUrl, 
																				   requestEntity,
					                                                               ResponseDTO.class);
			return response.getBody();
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			return new ResponseDTO(2, ErrorConstant.ERROR_CODE_INTERNAL_SERVER_ERROR, ErrorConstant.ERRMSG_INTERNAL_SERVER_ERROR);
		}
	}
	
	
	/**
	 * 
	 * <P>
	 * @param dto
	 * @return
	 */
	private FileUploadReponseDto callUploadFileApi (UploadVideoDto dto) {
		
		UploadVideoandThumbNailDto requestDto = new UploadVideoandThumbNailDto();
		requestDto.setThumbNail(dto.getThumbNail());
		requestDto.setVideo(dto.getVideo());
		
		String authToken = SourceInitializer.srcHdrMap.get(StreamItConstant.STREAMIT_UI);
		
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);
		httpHeaders.set(StreamItConstant.AUTHORIZATION, authToken);
		
		Resource multipartVideoResource  = requestDto.getVideo().getResource();  
		Resource multipartThumNailResource = requestDto.getThumbNail().getResource();
		
		LinkedMultiValueMap<String, Object> parts = new LinkedMultiValueMap<>();
		parts.add("thumbnail", multipartThumNailResource);
		parts.add("video", multipartVideoResource);
		
		HttpEntity<Object> requestEntity = new HttpEntity<>(parts, httpHeaders);
		
		try {
			ResponseEntity<FileUploadReponseDto> response = this.restTemplate.postForEntity(streamServiceUploadFileUrl, requestEntity, FileUploadReponseDto.class);
			return response.getBody();
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			return new FileUploadReponseDto(2, ErrorConstant.ERROR_CODE_INTERNAL_SERVER_ERROR, ErrorConstant.ERRMSG_INTERNAL_SERVER_ERROR);
		}
	}
	
	
	/**
	 * 
	 * <p>
	 * @param dto
	 * @return
	 */
	private ResponseDTO validateRequest(UploadVideoDto dto) {
		MultipartFile video = dto.getVideo();
		MultipartFile thumbNail = dto.getThumbNail();

		
		String originalFilename = video.getOriginalFilename();
		String videoExtension = StringUtils.getFilenameExtension(originalFilename);
		if (videoExtension == null) {
			return new ResponseDTO(2, ErrorConstant.ERROR_CODE_INVALID_VIDEO_UPLOAD, ErrorConstant.ERRMSG_INVALID_VIDEO_UPLOAD);
		}
		
		String thumbNailExtension = StringUtils.getFilenameExtension(thumbNail.getOriginalFilename());
		if (thumbNailExtension == null) {
			return new ResponseDTO(2, ErrorConstant.ERROR_CODE_MISSING_THUMBNAIL_TITLE, ErrorConstant.ERRMSG_MISSING_THUMBNAIL_TITLE);
		}
		
		String videoTitle = dto.getVideoTitle();
		String thumbNailTitle = dto.getThumbNailTitle();
		
		if (videoTitle == null || videoTitle.length() <= 0) {
			return new  ResponseDTO(2, ErrorConstant.ERROR_CODE_MISSING_VIDEO_TITLE, ErrorConstant.ERRMSG_MISSING_VIDEO_TITLE);
		} else if (thumbNailTitle == null || thumbNailTitle.length() <= 0) {
			return new ResponseDTO(2,ErrorConstant.ERROR_CODE_MISSING_THUMBNAIL, ErrorConstant.ERROR_CODE_MISSING_THUMBNAIL);
		}
	
	    return null;
	}
	
	/**
	 * 
	 * <p>
	 * @param ref
	 * @return
	 */
	public Mono<Resource> getVideoByRef (String ref) {
		
		Optional<Video> optionalVideo = this.videoRepo.findByvRef(ref);
		
		if (optionalVideo.isPresent()) {
			Video video = optionalVideo.get();
			String filePath = video.getvFilePath();
			return Mono.fromSupplier(()-> new FileSystemResource(filePath));
		}
		
		logger.info("Invlaid video ref {}", ref);
		return null;
	}
	
	/**
	 * 
	 * <p>
	 * @param searchParam
	 * @param pageable
	 * @return
	 */
	public Page<VideoListDto> getVideoList (Map<String, String> searchParam, Pageable pageable) {
		Page<Video> pageVideoList = null;
		if (searchParam.isEmpty()) {
			pageVideoList = this.videoRepo.findAll(pageable);
		} else {
			String videotitle = searchParam.get("video_name");
			logger.debug("Video title for search {}", videotitle);
		    pageVideoList = this.videoRepo.findByvName(videotitle, pageable);
		}
		long totalElements = pageVideoList.getTotalElements();
		List<Video> videoList = pageVideoList.getContent();
		pageVideoList = null;
		List<VideoListDto> dtoList = convertToDto(videoList);
		return new PageImpl<>(dtoList, pageable, totalElements);
	}
	
	/**
	 * 
	 * <p>
	 * @param videoList
	 * @return
	 */
	private List<VideoListDto> convertToDto (List <Video> videoList){
		List<VideoListDto> dataList = new  ArrayList<>();
		for (Video video : videoList) {
			VideoListDto dto = new VideoListDto(video.getvName(),video.getvRef(), video.getvCrdDt());
		    dataList.add(dto);
		}
		return dataList;
	} 
}
