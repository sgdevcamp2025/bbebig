package com.bbebig.serviceserver.global.feign.client;

import com.bbebig.commonmodule.clientDto.StateFeignResponseDto.ServerMemberPresenceResponseDto;
import com.bbebig.serviceserver.global.feign.fallback.StateFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "state-server", fallback = StateFallback.class)
public interface StateClient {
	@GetMapping("/feign/state/check/server/{serverId}/members")
	ServerMemberPresenceResponseDto checkServerMemberState(@PathVariable(value = "serverId") Long serverId);
}
