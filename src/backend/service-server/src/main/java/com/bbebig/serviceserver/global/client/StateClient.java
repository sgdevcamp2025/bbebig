package com.bbebig.serviceserver.global.client;

import com.bbebig.commonmodule.clientDto.StateFeignResponseDto.ServerMemberPresenceResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "state-server")
public interface StateClient {


	@GetMapping("/feign/state/check/server/{serverId}/members")
	ServerMemberPresenceResponseDto checkServerMemberState(@PathVariable(value = "serverId") Long serverId);
}
