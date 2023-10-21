package com.streamit.model.Dto;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonProperty;
 
public class UploadVideoDto {

	@JsonProperty("video_title")
	private String videoTitle;
	
	@JsonProperty("thumbnail_title")
	private String thumbNailTitle;
	
	@JsonProperty("video")
	private MultipartFile video;
	
	@JsonProperty("thumbnail")
	private MultipartFile thumbNail;
	
	@JsonProperty("userId")
	private Integer userId;

	public MultipartFile getThumbNail() {
		return thumbNail;
	}

	public void setThumbNail(MultipartFile thumbNail) {
		this.thumbNail = thumbNail;
	}

	public String getThumbNailTitle() {
		return thumbNailTitle;
	}

	public void setThumbNailTitle(String thumbNailTitle) {
		this.thumbNailTitle = thumbNailTitle;
	}

	public String getVideoTitle() {
		return videoTitle;
	}

	public void setVideoTitle(String videoTitle) {
		this.videoTitle = videoTitle;
	}

	public MultipartFile getVideo() {
		return video;
	}

	public void setVideo(MultipartFile video) {
		this.video = video;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public UploadVideoDto() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
}
