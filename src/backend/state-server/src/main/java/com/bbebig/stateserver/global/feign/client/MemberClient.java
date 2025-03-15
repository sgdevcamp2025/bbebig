package com.bbebig.stateserver.global.feign.client;

import com.bbebig.commonmodule.clientDto.UserFeignResponseDto.MemberCustomStatusResponseDto;
import com.bbebig.stateserver.global.feign.fallback.MemberFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@FeignClient(name = "user-server", fallback = MemberFallback.class)
public interface MemberClient {

	@GetMapping("/feign/members/{memberId}/global-status")
	MemberCustomStatusResponseDto getMemberCustomStatus(@PathVariable(value = "memberId") Long memberId);
}
