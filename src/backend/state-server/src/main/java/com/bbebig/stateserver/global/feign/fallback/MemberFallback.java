package com.bbebig.stateserver.global.feign.fallback;

import com.bbebig.commonmodule.clientDto.UserFeignResponseDto.MemberCustomStatusResponseDto;
import com.bbebig.commonmodule.global.response.code.error.ErrorStatus;
import com.bbebig.commonmodule.global.response.exception.ErrorHandler;
import com.bbebig.stateserver.global.feign.client.MemberClient;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MemberFallback implements MemberClient {

	@Override
	public MemberCustomStatusResponseDto getMemberCustomStatus(Long memberId) {
		log.warn("[State Fallback] getMemberCustomStatus() -> fallback triggered. memberId: {}", memberId);
		throw new ErrorHandler(ErrorStatus._INTERNAL_SERVER_ERROR);
	}
}
