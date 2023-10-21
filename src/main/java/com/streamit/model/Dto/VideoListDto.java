package com.streamit.model.Dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

public class VideoListDto {

	@JsonProperty("video_title")
	private String videoTitle;
	
	@JsonProperty("video_ref")
	private String videoRef;
	
	@JsonProperty("uploaded_on")
	@JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "dd-MM-yyyy HH:mm:ss", timezone = JsonFormat.DEFAULT_TIMEZONE )
	private Date crdDt;

	public VideoListDto(String videoTitle, String videoRef, Date crdDt) {
		super();
		this.videoTitle = videoTitle;
		this.videoRef = videoRef;
		this.crdDt = crdDt;
	}

	
}
