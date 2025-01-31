package com.bbebig.commonmodule.kafka.dto.serverEvent;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper=false)
public class ServerCategoryEventDto extends ServerEventDto{

	Long categoryId;

	String categoryName;

	String categoryType;

	Long order; // Category 순서, DELETE 시에는 null

	String status; // CREATE, UPDATE, DELETE
}
