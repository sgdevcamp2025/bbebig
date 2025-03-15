package com.bbebig.serviceserver.global.feign.client;

import com.bbebig.serviceserver.global.feign.fallback.MemberFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import static com.bbebig.commonmodule.clientDto.UserFeignResponseDto.*;

@FeignClient(name = "user-server", fallback = MemberFallback.class)
public interface MemberClient {

	@GetMapping("/feign/members/{memberId}")
	MemberInfoResponseDto getMemberInfo(@PathVariable(value = "memberId") Long memberId);
}
