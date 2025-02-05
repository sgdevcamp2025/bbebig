package com.bbebig.searchserver.domain.search.service;

import com.bbebig.searchserver.domain.search.domain.ChannelChatMessage;
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

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatMessageService {

	private final ChannelChatMessageRepository channelChatMessageRepository;
	private final DmChatMessageRepository dmChatMessageRepository;
	private final ChannelChatMessageElasticRepository channelChatMessageElasticRepository;
	private final DmChatMessageElasticRepository dmChatMessageElasticRepository;


	public void saveChannelMessage(ChatMessageDto messageDto) {
		ChannelChatMessage message = ChatMessageDtoConverter.convertDtoToChannelChatMessage(messageDto);
		channelChatMessageRepository.save(message);
		channelChatMessageElasticRepository.save(ChatMessageDtoConverter.convertDtoToChannelChatMessageElastic(messageDto));
	}


	public void saveDmMessage(ChatMessageDto messageDto) {
		DmChatMessage message = ChatMessageDtoConverter.convertDtoToDmChatMessage(messageDto);
		dmChatMessageRepository.save(message);
		dmChatMessageElasticRepository.save(ChatMessageDtoConverter.convertDtoToDmChatMessageElastic(messageDto));
	}

	public void updateChannelMessage(ChatMessageDto messageDto) {
		Optional<ChannelChatMessage> optionalMessage = channelChatMessageRepository.findById(messageDto.getId());

		optionalMessage.ifPresentOrElse(chatMessage -> {
			if (chatMessage.isDeleted()) {
				log.error("[Search] ChatMessageService : 삭제된 메시지는 수정할 수 없습니다. messageId: {}", messageDto.getId());
				throw new IllegalStateException("삭제된 메시지는 수정할 수 없습니다.");
			}

			chatMessage.updateContent(messageDto.getContent());
			channelChatMessageRepository.save(chatMessage);
			channelChatMessageElasticRepository.save(ChatMessageDtoConverter.convertDtoToChannelChatMessageElastic(messageDto));

		}, () -> {
			log.error("[Search] ChatMessageService : 채팅 메시지 정보 없음. messageId: {}", messageDto.getId());
			throw new IllegalArgumentException("채팅 메시지 정보 없음.");
		});
	}

	public void updateDmMessage(ChatMessageDto messageDto) {
		Optional<DmChatMessage> optionalMessage = dmChatMessageRepository.findById(messageDto.getId());

		optionalMessage.ifPresentOrElse(chatMessage -> {
			if (chatMessage.isDeleted()) {
				log.error("[Search] ChatMessageService : 삭제된 메시지는 수정할 수 없습니다. messageId: {}", messageDto.getId());
				throw new IllegalStateException("삭제된 메시지는 수정할 수 없습니다.");
			}

			chatMessage.updateContent(messageDto.getContent());
			dmChatMessageRepository.save(chatMessage);
			dmChatMessageElasticRepository.save(ChatMessageDtoConverter.convertDtoToDmChatMessageElastic(messageDto));

		}, () -> {
			log.error("[Search] ChatMessageService : 채팅 메시지 정보 없음. messageId: {}", messageDto.getId());
			throw new IllegalArgumentException("채팅 메시지 정보 없음.");
		});
	}

	public void deleteChannelMessage(Long messageId) {
		Optional<ChannelChatMessage> optionalMessage = channelChatMessageRepository.findById(messageId);

		optionalMessage.ifPresentOrElse(chatMessage -> {
			if (chatMessage.isDeleted()) {
				log.warn("[Search] ChatMessageService : 이미 삭제된 메시지입니다. messageId: {}", messageId);
				return;
			}

			chatMessage.delete();
			channelChatMessageRepository.save(chatMessage);
			channelChatMessageElasticRepository.deleteById(messageId); // Hard Delete in Elasticsearch
			log.info("[Search] ChatMessageService : 채널 메시지 삭제 완료. messageId: {}", messageId);
		}, () -> {
			log.error("[Search] ChatMessageService : 채널 채팅 메시지를 찾을 수 없습니다. messageId: {}", messageId);
			throw new IllegalArgumentException("채널 채팅 메시지 정보 없음.");
		});
	}

	public void deleteDmMessage(Long messageId) {
		Optional<DmChatMessage> optionalMessage = dmChatMessageRepository.findById(messageId);

		optionalMessage.ifPresentOrElse(chatMessage -> {
			if (chatMessage.isDeleted()) {
				log.warn("[Search] ChatMessageService : 이미 삭제된 메시지입니다. messageId: {}", messageId);
				return;
			}

			chatMessage.delete();
			dmChatMessageRepository.save(chatMessage);
			dmChatMessageElasticRepository.deleteById(messageId); // Hard Delete in Elasticsearch
			log.info("[Search] ChatMessageService : DM 메시지 삭제 완료. messageId: {}", messageId);
		}, () -> {
			log.error("[Search]ChatMessageService : DM 채팅 메시지를 찾을 수 없습니다. messageId: {}", messageId);
			throw new IllegalArgumentException("DM 채팅 메시지 정보 없음.");
		});
	}
}
