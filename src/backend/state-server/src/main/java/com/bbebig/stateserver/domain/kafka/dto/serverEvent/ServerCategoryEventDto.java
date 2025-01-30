package com.bbebig.chatserver.domain.kafka.dto.serverEvent;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ServerCategoryEventDto extends ServerEventDto{

	Long categoryId;

	String categoryName;

	String categoryType;

	Long order; // Category 순서, DELETE 시에는 null

	String status; // CREATE, UPDATE, DELETE
}
