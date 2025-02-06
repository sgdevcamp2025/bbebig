package com.bbebig.commonmodule.kafka.dto.serverEvent;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Data
@EqualsAndHashCode(callSuper=false)
public class ServerActionEventDto extends ServerEventDto {

	private String serverName;

	private String profileImageUrl;

	String status; // CREATE, UPDATE, DELETE
}
