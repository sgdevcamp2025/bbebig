package com.bbebig.searchserver.domain.search.service;

import com.bbebig.commonmodule.kafka.dto.ChatMessageDto;
import com.bbebig.searchserver.domain.history.service.HistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChannelMessageBatchService {

	private final HistoryService historyService;

	public void processChannelMessages(List<ChatMessageDto> messageBatch) {
		// 1) 메세지를 메시지 타입/동작에 따라 grouping
		List<ChatMessageDto> toCreate = new ArrayList<>();
		List<ChatMessageDto> toUpdate = new ArrayList<>();
		List<Long> toDeleteIds = new ArrayList<>();

		for (ChatMessageDto dto : messageBatch) {
			switch (dto.getType()) {
				case MESSAGE_CREATE:
					toCreate.add(dto);
					break;
				case MESSAGE_UPDATE:
					toUpdate.add(dto);
					break;
				case MESSAGE_DELETE:
					toDeleteIds.add(dto.getId());
					break;
				default:
					log.error("Unknown message type: {}", dto);
			}
		}

		// 2) bulk create
		for (ChatMessageDto dto : toCreate) {
			historyService.saveChannelMessage(dto);
		}

		// 3) bulk update
		for (ChatMessageDto dto : toUpdate) {
			historyService.updateChannelMessage(dto);
		}

		// 4) bulk delete
		for (Long id : toDeleteIds) {
			historyService.deleteChannelMessage(id);
		}

		log.info("[Search] ChannelMessageBatchService: batch process 성공. create={}, update={}, delete={}",
				toCreate.size(), toUpdate.size(), toDeleteIds.size());
	}

	public void processDmMessages(List<ChatMessageDto> messageBatch) {
		// 1) 메세지를 메시지 타입/동작에 따라 grouping
		List<ChatMessageDto> toCreate = new ArrayList<>();
		List<ChatMessageDto> toUpdate = new ArrayList<>();
		List<Long> toDeleteIds = new ArrayList<>();

		for (ChatMessageDto dto : messageBatch) {
			switch (dto.getType()) {
				case MESSAGE_CREATE:
					toCreate.add(dto);
					break;
				case MESSAGE_UPDATE:
					toUpdate.add(dto);
					break;
				case MESSAGE_DELETE:
					toDeleteIds.add(dto.getId());
					break;
				default:
					log.error("Unknown message type: {}", dto);
			}
		}

		// 2) bulk create
		for (ChatMessageDto dto : toCreate) {
			historyService.saveDmMessage(dto);
		}

		// 3) bulk update
		for (ChatMessageDto dto : toUpdate) {
			historyService.updateDmMessage(dto);
		}

		// 4) bulk delete
		for (Long id : toDeleteIds) {
			historyService.deleteDmMessage(id);
		}

		log.info("[Search] DmMessageBatchService: batch process 성공. create={}, update={}, delete={}",
				toCreate.size(), toUpdate.size(), toDeleteIds.size());
	}
}
