package com.bbebig.chatserver.domain.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

public class ServiceResponseDto {

	@Data
	@Builder
	public static class DmChannelInfoResponseDto {
		private Long id;
		private List<Long> memberIds;
	}
}
