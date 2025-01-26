package com.bbebig.chatserver.domain.client;

import com.bbebig.chatserver.domain.dto.response.ServiceResponseDto.DmChannelInfoResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "service-server")
public interface ServiceClient {

	@GetMapping("/service-server/getRoomList")
	DmChannelInfoResponseDto getDmChannelInfo(Long channelId);

}
