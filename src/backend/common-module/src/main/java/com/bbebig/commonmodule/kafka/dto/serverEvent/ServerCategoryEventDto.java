package com.bbebig.commonmodule.kafka.dto.serverEvent;

import com.bbebig.commonmodule.kafka.dto.serverEvent.status.ServerCategoryStatus;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper=false)
public class ServerCategoryEventDto extends ServerEventDto{

	private Long categoryId;

	private String categoryName;

	private int order; // Category 순서, DELETE 시에는 null

	private ServerCategoryStatus status; // CREATE, UPDATE, DELETE

	@JsonCreator
	public ServerCategoryEventDto(
			@JsonProperty("serverId") Long serverId,
			@JsonProperty("type") ServerEventType type,
			@JsonProperty("categoryId") Long categoryId,
			@JsonProperty("categoryName") String categoryName,
			@JsonProperty("order") int order,
			@JsonProperty("status") ServerCategoryStatus status
	) {
		super(serverId, type);
		this.categoryId = categoryId;
		this.categoryName = categoryName;
		this.order = order;
		this.status = status;
	}
}
