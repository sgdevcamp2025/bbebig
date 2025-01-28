package com.bbebig.searchserver.domain.search.service;

import com.bbebig.searchserver.domain.search.domain.ChannelChatMessageElastic;
import com.bbebig.searchserver.domain.search.domain.DmChatMessageElastic;
import com.bbebig.searchserver.domain.search.dto.SearchRequestDto.*;
import com.bbebig.searchserver.domain.search.repository.ChannelChatMessageElasticRepository;
import com.bbebig.searchserver.domain.search.repository.ChannelChatMessageRepository;
import com.bbebig.searchserver.domain.search.repository.DmChatMessageElasticRepository;
import com.bbebig.searchserver.domain.search.repository.DmChatMessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class SearchService {

	private final ChannelChatMessageRepository channelChatMessageRepository;
	private final DmChatMessageRepository dmChatMessageRepository;
	private final ChannelChatMessageElasticRepository channelChatMessageElasticRepository;
	private final DmChatMessageElasticRepository dmChatMessageElasticRepository;

	// Todo: 유저 정보를 Passport 로 부터 가져온 후 검증하거나 하는 로직을 추후 추가

	// 메시지 내용을 기준으로 채널 채팅 검색
	public Page<ChannelChatMessageElastic> searchChannelMessagesByContentAndDate(ServerChannelChatSearchRequestDto requestDto, Long serverId) {
		Pageable pageable = PageRequest.of(requestDto.getPage(), requestDto.getSize());
		return channelChatMessageElasticRepository.findByContentContainingAndServerId(requestDto.getKeyword(), serverId, pageable);
	}


	// 메시지 내용을 기준으로 DM 채팅 검색
	public Page<DmChatMessageElastic> searchDmMessagesByContent(DmChatSearchRequestDto requestDto, Long channelId) {
		Pageable pageable = PageRequest.of(requestDto.getPage(), requestDto.getSize());
		return dmChatMessageElasticRepository.findByContentContainingAndChannelId(requestDto.getKeyword(), channelId, pageable);
	}
}
