package com.bbebig.stateserver.client;

import com.bbebig.commonmodule.clientDto.serviceServer.CommonServiceServerClientResponseDto.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "service-server")
public interface ServiceClient {

	@GetMapping("/feign/servers/{serverId}/list/members")
	ServerMemberListResponseDto getServerMemberList(@PathVariable(value = "serverId") Long serverId);

	@GetMapping("/feign/servers/{serverId}/list/channel")
	ServerChannelListResponseDto getServerChannelList(@PathVariable(value = "serverId") Long serverId);

	@GetMapping("/feign/servers/members/{memberId}/list")
	MemberServerListResponseDto getMemberServerList(@PathVariable(value = "memberId") Long memberId);
}
