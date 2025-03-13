package com.bbebig.searchserver.global.feign.client;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "state-server")
public interface StateClient {

}
