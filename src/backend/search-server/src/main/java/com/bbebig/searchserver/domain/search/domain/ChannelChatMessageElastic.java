package com.bbebig.searchserver.domain.search.domain;


import lombok.Builder;
import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDateTime;

@Data
@Builder
@Document(indexName = "channel_chat_messages")
public class ChannelChatMessageElastic {

	@Field(type = FieldType.Long)
	private Long id;

	@Field(type = FieldType.Long)
	private Long serverId;

	@Field(type = FieldType.Long)
	private Long channelId;

	@Field(type = FieldType.Long)
	private Long sendMemberId;

	@Field(type = FieldType.Text)
	private String content;

	@Field(type = FieldType.Date)
	private LocalDateTime createdAt;
}
