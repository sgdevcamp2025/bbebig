package com.bbebig.searchserver.global.feign.client;

import com.bbebig.commonmodule.clientDto.ServiceFeignResponseDto.*;
import com.bbebig.searchserver.global.feign.fallback.ServiceFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "service-server",fallback = ServiceFallback.class)
public interface ServiceClient {

	@GetMapping("/feign/servers/{serverId}/list/member")
	ServerMemberListResponseDto getServerMemberList(@PathVariable(value = "serverId") Long serverId);

	@GetMapping("/feign/servers/{serverId}/list/channel")
	ServerChannelListResponseDto getServerChannelList(@PathVariable(value = "serverId") Long serverId);

	@GetMapping("/feign/servers/members/{memberId}/list")
	MemberServerListResponseDto getMemberServerList(@PathVariable(value = "memberId") Long memberId);

	@GetMapping("/feign/servers/{serverId}/channels/info/member/{memberId}")
	ServerLastInfoResponseDto getServerLastInfo(@PathVariable(value = "serverId") Long serverId, @PathVariable(value = "memberId") Long memberId);

	@GetMapping("/feign/channels/{channelId}/lastInfo/member/{memberId}")
	ChannelLastInfoResponseDto getChannelLastInfo(@PathVariable(value = "channelId") Long channelId, @PathVariable(value = "memberId") Long memberId);

}
