package com.bbebig.commonmodule.kafka.dto.serverEvent;

import com.bbebig.commonmodule.kafka.dto.model.ChannelType;
import com.bbebig.commonmodule.kafka.dto.serverEvent.status.ServerChannelStatus;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Data
@EqualsAndHashCode(callSuper=false)
public class ServerChannelEventDto extends ServerEventDto {

	private Long categoryId;

	private Long channelId;

	private String channelName;

	private String channelType; // CHAT, VOICE

	private int order; // Channel 순서, DELETE 시에는 null

	private ServerChannelStatus status; // CREATE, UPDATE, DELETE

	@JsonCreator
	public ServerChannelEventDto(
			@JsonProperty("serverId") Long serverId,
			@JsonProperty("type") ServerEventType type,
			@JsonProperty("categoryId") Long categoryId,
			@JsonProperty("channelId") Long channelId,
			@JsonProperty("channelName") String channelName,
			@JsonProperty("channelType") String channelType,
			@JsonProperty("order") int order,
			@JsonProperty("status") ServerChannelStatus status
	) {
		super(serverId, type);
		this.categoryId = categoryId;
		this.channelId = channelId;
		this.channelName = channelName;
		this.channelType = channelType;
		this.order = order;
		this.status = status;
	}

}
