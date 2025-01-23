package com.bbebig.chatserver.domain.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChatFileDto {

	private String fileName;
	private String fileUrl;
	private String fileType;
	private String fileSize;
}
