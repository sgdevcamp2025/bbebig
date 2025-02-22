package com.bbebig.commonmodule.clientDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class SearchFeignResponseDto {

	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class ServerChannelSequenceResponseDto {
		private Long channelId;
		private Long lastSequence;
	}
}
