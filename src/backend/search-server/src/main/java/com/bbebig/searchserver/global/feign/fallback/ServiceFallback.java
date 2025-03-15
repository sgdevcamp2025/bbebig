package com.bbebig.searchserver.global.feign.fallback;

import com.bbebig.commonmodule.clientDto.ServiceFeignResponseDto.*;
import com.bbebig.commonmodule.global.response.code.error.ErrorStatus;
import com.bbebig.commonmodule.global.response.exception.ErrorHandler;
import com.bbebig.searchserver.global.feign.client.ServiceClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Slf4j
public class ServiceFallback implements ServiceClient {

	@Override
	public ServerMemberListResponseDto getServerMemberList(Long serverId) {
		// 예시: 서버 목록 조회 실패 시 빈 응답을 주거나 캐시값을 주는 등의 처리
		log.warn("[Search Fallback] getServerMemberList() -> fallback triggered. serverId: {}", serverId);
		throw new ErrorHandler(ErrorStatus._INTERNAL_SERVER_ERROR);
	}

	// 다른 메서드들도 동일한 방식으로 구현
	@Override
	public ServerChannelListResponseDto getServerChannelList(Long serverId) {
		log.warn("[Search Fallback] getServerChannelList() -> fallback triggered. serverId: {}", serverId);
		throw new ErrorHandler(ErrorStatus._INTERNAL_SERVER_ERROR);
	}

	@Override
	public MemberServerListResponseDto getMemberServerList(Long memberId) {
		log.warn("[Search Fallback] getMemberServerList() -> fallback triggered. memberId: {}", memberId);
		throw new ErrorHandler(ErrorStatus._INTERNAL_SERVER_ERROR);
	}

	@Override
	public ServerLastInfoResponseDto getServerLastInfo(Long serverId, Long memberId) {
		log.warn("[Search Fallback] getServerLastInfo() -> fallback triggered. serverId: {}, memberId: {}", serverId, memberId);
		throw new ErrorHandler(ErrorStatus._INTERNAL_SERVER_ERROR);
	}

	@Override
	public ChannelLastInfoResponseDto getChannelLastInfo(Long channelId, Long memberId) {
		log.warn("[Search Fallback] getChannelLastInfo() -> fallback triggered. channelId: {}, memberId: {}", channelId, memberId);
		throw new ErrorHandler(ErrorStatus._INTERNAL_SERVER_ERROR);
	}
}

