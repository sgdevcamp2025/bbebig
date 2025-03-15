package com.bbebig.serviceserver.global.feign.fallback;

import com.bbebig.commonmodule.global.response.code.error.ErrorStatus;
import com.bbebig.commonmodule.global.response.exception.ErrorHandler;
import com.bbebig.serviceserver.global.feign.client.SearchClient;
import lombok.extern.slf4j.Slf4j;

import static com.bbebig.commonmodule.clientDto.SearchFeignResponseDto.*;

@Slf4j
public class SearchFallback implements SearchClient {

	@Override
	public ServerChannelSequenceResponseDto getChannelLastSequence(Long channelId) {
		log.warn("[Search Fallback] getChannelLastSequence() -> fallback triggered. channelId: {}", channelId);
		throw new ErrorHandler(ErrorStatus._INTERNAL_SERVER_ERROR);
	}
}
