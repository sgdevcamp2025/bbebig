package com.bbebig.searchserver.global.client;

import com.bbebig.commonmodule.clientDto.serviceServer.CommonServiceServerClientResponseDto;
import com.bbebig.commonmodule.clientDto.serviceServer.CommonServiceServerClientResponseDto.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "service-server")
public interface ServiceClient {

	@GetMapping("/servers/{serverId}/list/member")
	ServerMemberListResponseDto getServerMemberList(@PathVariable(value = "serverId") Long serverId);

	@GetMapping("/servers/{serverId}/list/channel")
	ServerChannelListResponseDto getServerChannelList(@PathVariable(value = "serverId") Long serverId);

	@GetMapping("/servers/members/{memberId}/list")
	MemberServerListResponseDto getMemberServerList(@PathVariable(value = "memberId") Long memberId);

	@GetMapping("/servers/{serverId}/channels/info/member/{memberId}")
	ServerLastInfoResponseDto getServerLastInfo(@PathVariable(value = "serverId") Long serverId, @PathVariable(value = "memberId") Long memberId);

	@GetMapping("/channels/{channelId}/recentInfo/member/{memberId}")
	ChannelLastInfoResponseDto getChannelLastInfo(@PathVariable(value = "channelId") Long channelId, @PathVariable(value = "memberId") Long memberId);

}
