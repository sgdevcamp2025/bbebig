package com.bbebig.chatserver.domain.chat.client;

import com.bbebig.commonmodule.clientDto.UserFeignResponseDto.MemberInfoResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-server")
public interface MemberClient {

	@GetMapping("/feign/members/{memberId}")
	MemberInfoResponseDto getMemberInfo(@PathVariable(value = "memberId") Long memberId);
}
