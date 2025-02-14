package com.bbebig.commonmodule.kafka.dto.serverEvent;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper=false)
public class ServerCategoryEventDto extends ServerEventDto{

	Long categoryId;

	String categoryName;

	int order; // Category 순서, DELETE 시에는 null

	String status; // CREATE, UPDATE, DELETE

	@JsonCreator
	public ServerCategoryEventDto(
			@JsonProperty("serverId") Long serverId,
			@JsonProperty("serverEventType") ServerEventType serverEventType,
			@JsonProperty("categoryId") Long categoryId,
			@JsonProperty("categoryName") String categoryName,
			@JsonProperty("order") int order,
			@JsonProperty("status") String status
	) {
		super(serverId, serverEventType);
		this.categoryId = categoryId;
		this.categoryName = categoryName;
		this.order = order;
		this.status = status;
	}
}
