package com.bbebig.stateserver.global.feign.fallback;

import com.bbebig.commonmodule.clientDto.ServiceFeignResponseDto;
import com.bbebig.commonmodule.global.response.code.error.ErrorStatus;
import com.bbebig.commonmodule.global.response.exception.ErrorHandler;
import com.bbebig.stateserver.global.feign.client.ServiceClient;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ServiceFallback implements ServiceClient {


	@Override
	public ServiceFeignResponseDto.ServerMemberListResponseDto getServerMemberList(Long serverId) {
		log.warn("[State Fallback] getServerMemberList() -> fallback triggered. serverId: {}", serverId);
		throw new ErrorHandler(ErrorStatus._INTERNAL_SERVER_ERROR);
	}

	@Override
	public ServiceFeignResponseDto.ServerChannelListResponseDto getServerChannelList(Long serverId) {
		log.warn("[State Fallback] getServerChannelList() -> fallback triggered. serverId: {}", serverId);
		throw new ErrorHandler(ErrorStatus._INTERNAL_SERVER_ERROR);
	}

	@Override
	public ServiceFeignResponseDto.MemberServerListResponseDto getMemberServerList(Long memberId) {
		log.warn("[State Fallback] getMemberServerList() -> fallback triggered. memberId: {}", memberId);
		throw new ErrorHandler(ErrorStatus._INTERNAL_SERVER_ERROR);
	}
}
