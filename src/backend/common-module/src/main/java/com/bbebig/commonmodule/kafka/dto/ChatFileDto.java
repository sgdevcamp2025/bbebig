package com.bbebig.commonmodule.kafka.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChatFileDto {

	private String fileName;
	private String fileUrl;
	private String fileType;
	private String fileSize;

	@JsonCreator
	public ChatFileDto(@JsonProperty("fileName") String fileName,
					   @JsonProperty("fileUrl") String fileUrl,
					   @JsonProperty("fileType") String fileType,
					   @JsonProperty("fileSize") String fileSize) {
		this.fileName = fileName;
		this.fileUrl = fileUrl;
		this.fileType = fileType;
		this.fileSize = fileSize;
	}
}
