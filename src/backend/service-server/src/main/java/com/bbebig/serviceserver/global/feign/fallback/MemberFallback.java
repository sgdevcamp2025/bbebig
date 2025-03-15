package com.bbebig.serviceserver.global.feign.fallback;

import com.bbebig.commonmodule.clientDto.UserFeignResponseDto.MemberInfoResponseDto;
import com.bbebig.commonmodule.global.response.code.error.ErrorStatus;
import com.bbebig.commonmodule.global.response.exception.ErrorHandler;
import com.bbebig.serviceserver.global.feign.client.MemberClient;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MemberFallback implements MemberClient {

	@Override
	public MemberInfoResponseDto getMemberInfo(Long memberId) {
		log.warn("[Service Fallback] getMemberInfo() -> fallback triggered. memberId: {}", memberId);
		throw new ErrorHandler(ErrorStatus._INTERNAL_SERVER_ERROR);
	}
}
