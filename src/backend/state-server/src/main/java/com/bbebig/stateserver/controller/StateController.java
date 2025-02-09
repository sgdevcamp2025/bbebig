package com.bbebig.stateserver.controller;

import com.bbebig.commonmodule.clientDto.state.CommonStateClientResponseDto;
import com.bbebig.commonmodule.clientDto.stateServer.CommonStateServerClientResponseDto;
import com.bbebig.commonmodule.global.response.code.CommonResponse;
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
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/state")
@RequiredArgsConstructor
@Tag(name = "서버", description = "서버 관련 API")
public class StateController {

	private final StateService stateService;


	// 사용자 상태 확인
	@Operation(summary = "사용자 상태 확인", description = "사용자의 현재 상태와 실제 상태를 확인한다.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "상태 확인 성공", useReturnTypeSchema = true),
			@ApiResponse(responseCode = "400", description = "", content = @Content)
	})
	@GetMapping("/check/member/{memberId}")
	public CommonResponse<MemberStatusResponseDto> checkMemberState(@PathVariable @Parameter(description = "사용자 ID") Long memberId) {
		log.info("[State] Check member state: {}", memberId);
		return CommonResponse.onSuccess(stateService.checkMemberState(memberId));
	}

	// 서버 멤버 상태 확인
	@Operation(summary = "서버 멤버 상태 확인", description = "서버의 멤버들의 상태를 확인한다.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "상태 확인 성공", useReturnTypeSchema = true),
			@ApiResponse(responseCode = "400", description = "", content = @Content)
	})
	@GetMapping("/check/server/{serverId}/members")
	public CommonResponse<ServerMemberPresenceResponseDto> checkServerMembersState(@PathVariable @Parameter(description = "서버 ID") Long serverId) {
		log.info("[State] Check server members state: {}", serverId);
		return CommonResponse.onSuccess(stateService.checkServerMembersState(serverId));
	}


}
