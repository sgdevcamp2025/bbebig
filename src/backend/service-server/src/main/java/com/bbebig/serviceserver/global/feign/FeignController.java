package com.bbebig.serviceserver.global.feign;

import com.bbebig.commonmodule.clientDto.ServiceFeignResponseDto.*;
import com.bbebig.commonmodule.global.response.code.CommonResponse;
import com.bbebig.commonmodule.redis.domain.ChannelLastInfo;
import com.bbebig.serviceserver.channel.service.ChannelService;
import com.bbebig.serviceserver.server.service.ServerService;
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


@Slf4j
@RestController
@RequestMapping("/feign")
@RequiredArgsConstructor
@Tag(name = "Feign", description = "서버 내부 호출에서 사용되는 관련 API")
public class FeignController {

	private final ServerService serverService;
	private final ChannelService channelService;

	@Operation(summary = "서버에 속해있는 채널 목록 조회 (For FeignClient)", description = "서버에 속해있는 채널 목록을 조회합니다.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "서버에 속해있는 채널 목록 조회 성공", useReturnTypeSchema = true),
			@ApiResponse(responseCode = "400", description = "", content = @Content)
	})
	@GetMapping("/servers/{serverId}/list/channel")
	public ServerChannelListResponseDto getServerChannelList(
			@PathVariable Long serverId
	) {
		log.info("[Service] 서버에 속해있는 채널 목록 조회 요청: serverId = {}", serverId);
		return serverService.getServerChannelList(serverId);
	}

	@Operation(summary = "서버에 속해있는 멤버 아이디 조회 (For FeignClient)", description = "서버에 속해있는 멤버 목록을 조회합니다.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "서버에 속해있는 멤버 목록 조회 성공", useReturnTypeSchema = true),
			@ApiResponse(responseCode = "400", description = "", content = @Content)
	})
	@GetMapping("/servers/{serverId}/list/members")
	public ServerMemberListResponseDto getServerMemberList(
			@PathVariable Long serverId
	) {
		log.info("[Service] 서버에 속해있는 멤버 아이디 목록 조회 요청: serverId = {}", serverId);
		return serverService.getServerMemberIdList(serverId);
	}

	@Operation(summary = "멤버별로 속해있는 서버 목록 조회 (For FeignClient)", description = "멤버별로 속해있는 서버 목록 조회합니다.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "멤버별로 속해있는 서버 목록 조회 성공", useReturnTypeSchema = true),
			@ApiResponse(responseCode = "400", description = "", content = @Content)
	})
	@GetMapping("/servers/members/{memberId}/list")
	public MemberServerListResponseDto getMemberServerList(
			@PathVariable Long memberId
	) {
		log.info("[Service] 멤버별로 속해있는 서버 목록 조회 요청 (For FeignClient): memberId = {}", memberId);
		return serverService.getMemberServerList(memberId);
	}

	@Operation(summary = "서버별 채널 마지막 방문 정보 조회 (For FeignClient)", description = "서버별 채널 마지막 방문 정보를 조회합니다.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "채널 마지막 방문 정보 조회 성공", useReturnTypeSchema = true),
			@ApiResponse(responseCode = "400", description = "", content = @Content)
	})
	@GetMapping("/servers/{serverId}/channels/info/member/{memberId}")
	public ServerLastInfoResponseDto getServerLastInfo(
			@PathVariable Long serverId,
			@PathVariable Long memberId
	) {
		log.info("[Service] 서버별 채널 마지막 방문 정보 조회 요청: serverId = {}, memberId = {}", serverId, memberId);
		return serverService.getServerChannelLastInfoForApi(memberId, serverId);
	}

	@Operation(summary = "채널 마지막 방문 정보 조회(For FeignClient)", description = "채널 마지막 방문 정보를 조회합니다.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "채널 마지막 방문 정보 조회 성공", useReturnTypeSchema = true),
			@ApiResponse(responseCode = "400", description = "", content = @Content)
	})
	@GetMapping("/channels/{channelId}/lastInfo/member/{memberId}")
	public ChannelLastInfo getChannelLastInfo(@PathVariable Long channelId, @PathVariable Long memberId) {
		log.info("[Service] 채널 마지막 방문 정보 조회 요청: channelId = {}, memberId = {}", channelId, memberId);
		return channelService.getChannelLastInfo(channelId, memberId);
	}
}
