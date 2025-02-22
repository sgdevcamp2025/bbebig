package com.bbebig.serviceserver.global.client;

import com.bbebig.commonmodule.clientDto.SearchFeignResponseDto.ServerChannelSequenceResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "search-server")
public interface SearchClient {

	@GetMapping("/feign/channels/{channelId}/sequence")
	ServerChannelSequenceResponseDto getChannelLastSequence(@PathVariable(value = "channelId") Long channelId);
}
