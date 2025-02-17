package com.bbebig.userserver.global.feign;

import com.bbebig.commonmodule.global.response.code.CommonResponse;
import com.bbebig.userserver.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.bbebig.userserver.global.feign.clientDto.UserFeignResponseDto.*;

@Slf4j
@RestController
@RequestMapping("/feign")
@RequiredArgsConstructor
@Tag(name = "Feign", description = "서버 내부 호출에서 사용되는 관련 API")
public class FeignController {

	private final MemberService memberService;

	@Operation(summary = "멤버 정보 조회 (For Feign Client)", description = "멤버 정보를 조회 (For Feign Client)")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "멤버 조회 성공", useReturnTypeSchema = true),
			@ApiResponse(responseCode = "400", description = "", content = @Content)
	})
	@GetMapping("/members/{memberId}")
	public CommonResponse<MemberInfoResponseDto> getMemberInfo(
			@PathVariable Long memberId
	) {
		log.info("[Member] 멤버 정보 조회 요청: memberId = {}", memberId);
		return CommonResponse.onSuccess(memberService.getMemberInfo(memberId));
	}

	@Operation(summary = "멤버 전역 상태 조회 (For Feign Client)", description = "멤버 전역 상태를 조회합니다. (For Feign Client)")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "멤버 전역 상태 조회 성공", useReturnTypeSchema = true),
			@ApiResponse(responseCode = "400", description = "", content = @Content)
	})
	@GetMapping("/members/{memberId}/global-status")
	public CommonResponse<MemberGlobalStatusResponseDto> getMemberGlobalStatus(
			@PathVariable Long memberId
	) {
		log.info("[Member] 멤버 전역 상태 조회 요청: memberId = {}", memberId);
		return CommonResponse.onSuccess(memberService.getMemberGlobalStatus(memberId));
	}
}
