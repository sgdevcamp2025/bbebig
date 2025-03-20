package com.bbebig.serviceserver.global.feign.client;

import com.bbebig.commonmodule.clientDto.SearchFeignResponseDto.ServerChannelSequenceResponseDto;
import com.bbebig.serviceserver.global.feign.fallback.SearchFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "search-server", fallback = SearchFallback.class)
public interface SearchClient {

	@GetMapping("/feign/channels/{channelId}/sequence")
	ServerChannelSequenceResponseDto getChannelLastSequence(@PathVariable(value = "channelId") Long channelId);
}
