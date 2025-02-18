package com.bbebig.chatserver.domain.chat.client;

import com.bbebig.chatserver.domain.chat.dto.response.ServiceResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "service-server")
public interface ServiceClient {

}
