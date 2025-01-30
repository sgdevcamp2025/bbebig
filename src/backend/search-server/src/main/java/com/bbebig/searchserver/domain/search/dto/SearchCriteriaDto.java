package com.bbebig.searchserver.domain.search.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class SearchCriteriaDto {

	private String keyword;

	private Long senderId;

	private LocalDateTime afterDate;

	private LocalDateTime beforeDate;

	private LocalDateTime exactDate;

	private List<SearchOption> options;
}
