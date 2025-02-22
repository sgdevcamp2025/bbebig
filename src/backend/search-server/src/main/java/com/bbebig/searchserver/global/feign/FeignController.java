package com.bbebig.searchserver.global.feign;


import com.bbebig.commonmodule.clientDto.SearchFeignResponseDto;
import com.bbebig.commonmodule.clientDto.SearchFeignResponseDto.ServerChannelSequenceResponseDto;
import com.bbebig.searchserver.domain.history.service.HistoryService;
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

	private final HistoryService historyService;

	@GetMapping("/channels/{channelId}/sequence")
	public ServerChannelSequenceResponseDto getChannelLastSequence(@PathVariable(value = "channelId") Long channelId) {
		log.info("[Feign] 채널 마지막 시퀀스 조회 요청: channelId = {}", channelId);
		return historyService.getChannelLastSequence(channelId);
	}
}
