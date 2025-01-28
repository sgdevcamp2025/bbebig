package com.bbebig.searchserver.domain.search.service;

import com.bbebig.searchserver.domain.search.domain.ChannelChatMessage;
import com.bbebig.searchserver.domain.search.domain.DmChatMessage;
import com.bbebig.searchserver.domain.search.dto.ChannelChatMessageConverter;
import com.bbebig.searchserver.domain.search.repository.ChannelChatMessageElasticRepository;
import com.bbebig.searchserver.domain.search.repository.ChannelChatMessageRepository;
import com.bbebig.searchserver.domain.search.repository.DmChatMessageElasticRepository;
import com.bbebig.searchserver.domain.search.repository.DmChatMessageRepository;
import com.bbebig.searchserver.global.kafka.dto.ChatMessageDto;
import com.bbebig.searchserver.global.kafka.model.ChannelType;
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
		ChannelChatMessage channelChatMessage = ChannelChatMessageConverter.convertDtoToChannelChatMessage(messageDto);
		channelChatMessageRepository.save(channelChatMessage);
	}

	public void saveDmMessageToMongo(ChatMessageDto messageDto) {
		DmChatMessage dmChatMessage = ChannelChatMessageConverter.convertDtoToDmChatMessage(messageDto);
		dmChatMessageRepository.save(dmChatMessage);
	}
}
