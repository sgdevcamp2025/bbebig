package com.bbebig.stateserver.client;

import com.bbebig.stateserver.dto.ServiceResponseDto.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "service-server")
public interface ServiceClient {

	@GetMapping("/server/info/{serverId}/list/member")
	ServerMemberListResponseDto getServerMemberList(@PathVariable(value = "serverId") Long serverId);

	@GetMapping("info/list/server/{memberId}")
	MemberServerListResponseDto getMemberServerList(@PathVariable(value = "memberId") Long memberId);

	@GetMapping("/dm/info/{channelId}/list/member")
	DmMemberListResponseDto getDmMemberList(@PathVariable(value = "channelId") Long channelId);
}
