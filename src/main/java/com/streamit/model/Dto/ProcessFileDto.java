package com.streamit.model.Dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ProcessFileDto {
	
	@JsonProperty("video_title")
	private String videoTitle;
	
	@JsonProperty("thumb_nail_title")
	private String thumbNailTitle;
	
	@JsonProperty("video_file_path")
	private  String videoFilePath;
	
	@JsonProperty("video_file_name")
	private String videoFileName;
	
	@JsonProperty("thumb_file_path")
	private String thumbnailFilePath;
	
	@JsonProperty("thumb_file_name")
	private String thubnailFileName;
	
	@JsonProperty("user_id")
	private Integer userId;
	
	
	

	public ProcessFileDto(String videoTitle, String thumbNailTitle, String videoFilePath, String videoFileName,
			String thumbnailFilePath, String thubnailFileName, Integer userId) {
		super();
		this.videoTitle = videoTitle;
		this.thumbNailTitle = thumbNailTitle;
		this.videoFilePath = videoFilePath;
		this.videoFileName = videoFileName;
		this.thumbnailFilePath = thumbnailFilePath;
		this.thubnailFileName = thubnailFileName;
		this.userId = userId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getVideoTitle() {
		return videoTitle;
	}

	public void setVideoTitle(String videoTitle) {
		this.videoTitle = videoTitle;
	}

	public String getThumbNailTitle() {
		return thumbNailTitle;
	}

	public void setThumbNailTitle(String thumbNailTitle) {
		this.thumbNailTitle = thumbNailTitle;
	}

	public String getVideoFilePath() {
		return videoFilePath;
	}

	public void setVideoFilePath(String videoFilePath) {
		this.videoFilePath = videoFilePath;
	}

	public String getVideoFileName() {
		return videoFileName;
	}

	public void setVideoFileName(String videoFileName) {
		this.videoFileName = videoFileName;
	}

	public String getThumbnailFilePath() {
		return thumbnailFilePath;
	}

	public void setThumbnailFilePath(String thumbnailFilePath) {
		this.thumbnailFilePath = thumbnailFilePath;
	}

	public String getThubnailFileName() {
		return thubnailFileName;
	}

	public void setThubnailFileName(String thubnailFileName) {
		this.thubnailFileName = thubnailFileName;
	}
	

}
