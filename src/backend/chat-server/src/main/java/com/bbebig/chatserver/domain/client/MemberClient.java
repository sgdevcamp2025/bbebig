package com.bbebig.chatserver.domain.client;

import com.bbebig.chatserver.domain.dto.response.MemberResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "member-server")
public interface MemberClient {

	@GetMapping("/member-server/info/{memberId}")
	MemberResponseDto getMemberInfo(@PathVariable(value = "memberId") Long memberId);
}
