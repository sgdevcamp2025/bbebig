package com.bbebig.stateserver.client;

import com.bbebig.commonmodule.clientDto.userServer.CommonUserServerResponseDto.MemberGlobalStatusResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@FeignClient(name = "user-server")
public interface MemberClient {

	@GetMapping("/members/{memberId}/global-status")
	MemberGlobalStatusResponseDto getMemberGlobalStatus(@PathVariable(value = "memberId") Long memberId);
}
