package com.bbebig.searchserver.domain.search.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

public class SearchRequestDto {

	/**
	 * 기본 채널 채팅 검색 DTO
	 */
	@Data
	@Builder
	public static class ServerChannelChatSearchRequestDto {
		private String keyword;
		private int page;
		private int size;
	}

	/**
	 * DM 채팅 기본 검색 DTO
	 */
	@Data
	@Builder
	public static class DmChatSearchRequestDto {
		private String keyword;
		private int page;
		private int size;
	}


}
