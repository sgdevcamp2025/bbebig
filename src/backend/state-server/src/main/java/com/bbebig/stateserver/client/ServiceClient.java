package com.bbebig.stateserver.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "service-server")
public interface ServiceClient {

//	@GetMapping("/service-server/list/{memberId}")

}
