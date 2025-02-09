package com.bbebig.commonmodule.kafka.dto.serverEvent;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.util.List;

@SuperBuilder
@Data
@EqualsAndHashCode(callSuper=false)
public class ServerActionEventDto extends ServerEventDto {

	private String serverName;

	private String profileImageUrl;

	String status; // CREATE, UPDATE, DELETE

//	private List<Long> channelIdList; // status가 CREATE일 경우에만 필요
}
