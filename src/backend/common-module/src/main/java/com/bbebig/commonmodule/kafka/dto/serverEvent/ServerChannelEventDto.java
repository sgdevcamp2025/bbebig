package com.bbebig.commonmodule.kafka.dto.serverEvent;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ServerChannelEventDto extends ServerEventDto {

	Long categoryId;

	Long channelId;

	String channelName;

	String channelType;

	Long order; // Channel 순서, DELETE 시에는 null

	String status; // CREATE, UPDATE, DELETE

}
