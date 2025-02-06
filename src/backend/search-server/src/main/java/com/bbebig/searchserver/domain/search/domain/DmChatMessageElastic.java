package com.bbebig.searchserver.domain.search.domain;


import lombok.Builder;
import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@Document(indexName = "dm_chat_messages")
public class DmChatMessageElastic {

	@Field(type = FieldType.Long)
	private Long id;

	@Field(type = FieldType.Long)
	private Long channelId;

	@Field(type = FieldType.Long)
	private Long sendMemberId;

	@Field(type = FieldType.Text)
	private String content;

	@Field(type = FieldType.Date)
	private LocalDateTime createdAt;

}