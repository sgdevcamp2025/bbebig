package com.bbebig.searchserver.domain.search.client;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "state-server")
public interface StateClient {

}
