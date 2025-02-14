package com.bbebig.stateserver.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import static com.bbebig.stateserver.dto.MemberResponseDto.*;

@FeignClient(name = "member-server")
public interface MemberClient {

	@GetMapping("/info/{memberId}/status-global")
	MemberGlobalStatusResponseDto getMemberGlobalStatus(@PathVariable(value = "memberId") Long memberId);
}
