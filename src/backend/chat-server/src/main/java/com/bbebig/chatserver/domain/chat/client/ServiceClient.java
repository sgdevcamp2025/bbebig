package com.bbebig.chatserver.domain.chat.client;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "service-server")
public interface ServiceClient {

}
