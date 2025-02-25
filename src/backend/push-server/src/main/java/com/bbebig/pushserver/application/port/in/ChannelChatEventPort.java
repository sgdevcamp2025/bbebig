package com.bbebig.pushserver.application.port.in;

import com.bbebig.commonmodule.kafka.dto.ChatMessageDto;

public interface ChannelChatEventPort {

	void handleChannelChatEvent(ChatMessageDto chatMessageDto);
}
