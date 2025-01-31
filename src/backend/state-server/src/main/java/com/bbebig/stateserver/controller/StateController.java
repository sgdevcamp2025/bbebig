package com.bbebig.stateserver.controller;

import com.bbebig.commonmodule.global.response.code.CommonResponse;
import com.bbebig.commonmodule.passport.annotation.PassportUser;
import com.bbebig.commonmodule.proto.PassportProto;
import com.bbebig.commonmodule.proto.PassportProto.Passport;
import com.bbebig.stateserver.dto.StateResponseDto.*;
import com.bbebig.stateserver.service.StateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/state-server")
@RequiredArgsConstructor
@Tag(name = "서버", description = "서버 관련 API")
public class StateController {

	private final StateService stateService;


	// 사용자 상태 확인
	@Operation(summary = "사용자 상태 확인", description = "사용자의 현재 상태와 실제 상태를 확인한다.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "서버 생성 성공", useReturnTypeSchema = true),
			@ApiResponse(responseCode = "400", description = "", content = @Content)
	})
	@GetMapping("/state/check/member")
	public CommonResponse<MemberStatusResponseDto> checkMemberState(
			@Parameter(hidden = true) @PassportUser Passport passport) {
		log.info("[State] Check member state: {}", passport.getMemberId());
		return CommonResponse.onSuccess(stateService.checkMemberState(passport.getMemberId()));
	}
}
