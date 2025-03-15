package com.bbebig.serviceserver.global.feign.fallback;

import com.bbebig.commonmodule.clientDto.StateFeignResponseDto.ServerMemberPresenceResponseDto;
import com.bbebig.commonmodule.global.response.code.error.ErrorStatus;
import com.bbebig.commonmodule.global.response.exception.ErrorHandler;
import com.bbebig.serviceserver.global.feign.client.StateClient;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StateFallback implements StateClient {

	@Override
	public ServerMemberPresenceResponseDto checkServerMemberState(Long serverId) {
		log.warn("[Service Fallback] checkServerMemberState() -> fallback triggered. serverId: {}", serverId);
		throw new ErrorHandler(ErrorStatus._INTERNAL_SERVER_ERROR);
	}

}
