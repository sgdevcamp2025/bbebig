package com.bbebig.serviceserver.global.client;

import com.bbebig.commonmodule.clientDto.userServer.CommonUserServerResponseDto.MemberInfoResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-server")
public interface MemberClient {

	@GetMapping("/members/{memberId}/info")
	MemberInfoResponseDto getMemberInfo(@PathVariable(value = "memberId") Long memberId);
}
