package com.bbebig.searchserver.domain.search.service;

import com.bbebig.searchserver.domain.search.domain.ChannelChatMessage;
import com.bbebig.searchserver.domain.search.domain.ChannelChatMessageElastic;
import com.bbebig.searchserver.domain.search.domain.DmChatMessage;
import com.bbebig.searchserver.domain.search.dto.ChatMessageDtoConverter;
import com.bbebig.searchserver.domain.search.repository.ChannelChatMessageElasticRepository;
import com.bbebig.searchserver.domain.search.repository.ChannelChatMessageRepository;
import com.bbebig.searchserver.domain.search.repository.DmChatMessageElasticRepository;
import com.bbebig.searchserver.domain.search.repository.DmChatMessageRepository;
import com.bbebig.searchserver.global.kafka.dto.ChatMessageDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatMessageService {

	private final ChannelChatMessageRepository channelChatMessageRepository;
	private final DmChatMessageRepository dmChatMessageRepository;
	private final ChannelChatMessageElasticRepository channelChatMessageElasticRepository;
	private final DmChatMessageElasticRepository dmChatMessageElasticRepository;


	public void saveChannelMessageToMongo(ChatMessageDto messageDto) {
		channelChatMessageRepository.save(ChatMessageDtoConverter.convertDtoToChannelChatMessage(messageDto));
	}

	public void saveDmMessageToMongo(ChatMessageDto messageDto) {
		dmChatMessageRepository.save(ChatMessageDtoConverter.convertDtoToDmChatMessage(messageDto));
	}

	public void saveChannelMessageToElastic(ChatMessageDto messageDto) {
		channelChatMessageElasticRepository.save(ChatMessageDtoConverter.convertDtoToChannelChatMessageElastic(messageDto));
	}

	public void saveDmMessageToElastic(ChatMessageDto messageDto) {
		dmChatMessageElasticRepository.save(ChatMessageDtoConverter.convertDtoToDmChatMessageElastic(messageDto));
	}
}
