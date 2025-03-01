package com.bbebig.serviceserver.server.service;

import com.bbebig.serviceserver.server.dto.response.PassportTestResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "passportClient", url = "http://43.203.136.82:9080")
public interface PassportTestClient {

    @GetMapping("/passports")
    PassportTestResponse getMemberIdByJwt(@RequestParam("jwt") String jwt);
}