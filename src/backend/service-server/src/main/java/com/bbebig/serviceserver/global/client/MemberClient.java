package com.bbebig.serviceserver.global.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import static com.bbebig.commonmodule.clientDto.UserFeignResponseDto.*;

@FeignClient(name = "user-server")
public interface MemberClient {

	@GetMapping("/feign/members/{memberId}")
	MemberInfoResponseDto getMemberInfo(@PathVariable(value = "memberId") Long memberId);
}
