package com.bbebig.searchserver.domain.search.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

public class SearchRequestDto {

	/**
	 * 기본 채널 채팅 검색 DTO
	 */
	@Data
	@Builder
	public static class SearchMessageRequestDto {
		private String keyword;
		private Long senderId;
		private LocalDateTime afterDate;
		private LocalDateTime beforeDate;
		private LocalDateTime exactDate;
		private List<SearchOption> options;
		private int page;
		private int size;
	}

}
