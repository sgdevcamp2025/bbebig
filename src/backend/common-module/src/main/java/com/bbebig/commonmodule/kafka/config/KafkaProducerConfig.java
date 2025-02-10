package com.bbebig.commonmodule.kafka.config;

import com.bbebig.commonmodule.kafka.dto.*;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
public class KafkaProducerConfig {

	@Value("${spring.kafka.bootstrap-servers}")
	private String bootstrapAddress;

	@Bean
	public Map<String, Object> producerConfigurations(){
		Map<String, Object> configurations = new HashMap<>();
		configurations.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
		configurations.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		configurations.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
		configurations.put(ProducerConfig.ACKS_CONFIG, "all"); // 메시지 전송 보장 수준
		configurations.put(ProducerConfig.RETRIES_CONFIG, 3);  // 전송 실패 시 재시도 횟수
		return configurations;
	}

	// 채팅 Producer 생성
	@Bean
	public ProducerFactory<String, ChatMessageDto> producerFactoryForChatEvent(){
		return new DefaultKafkaProducerFactory<>(producerConfigurations());
	}

	@Bean
	public KafkaTemplate<String, ChatMessageDto> kafkaTemplateForChat(){
		return new KafkaTemplate<>(producerFactoryForChatEvent());
	}

	// 세션 이벤트(웹소켓 연결) Producer
	@Bean
	public ProducerFactory<String, ConnectionEventDto> producerFactoryForConnectionEvent(){
		return new DefaultKafkaProducerFactory<>(producerConfigurations());
	}

	@Bean
	public KafkaTemplate<String, ConnectionEventDto> kafkaTemplateForConnection(){
		return new KafkaTemplate<>(producerFactoryForConnectionEvent());
	}

	// 채널 이벤트 (채널을 현재 보고있음, 떠남) Producer
	@Bean
	public ProducerFactory<String, ChannelEventDto> producerFactoryForChannelEvent(){
		return new DefaultKafkaProducerFactory<>(producerConfigurations());
	}

	@Bean
	public KafkaTemplate<String, ChannelEventDto> kafkaTemplateForChannelEvent(){
		return new KafkaTemplate<>(producerFactoryForChannelEvent());
	}

	@Bean
	public ProducerFactory<String, PresenceEventDto> producerFactoryForPresenceEvent(){
		return new DefaultKafkaProducerFactory<>(producerConfigurations());
	}

	@Bean
	public KafkaTemplate<String, PresenceEventDto> kafkaTemplateForPresenceEvent(){
		return new KafkaTemplate<>(producerFactoryForPresenceEvent());
	}

	@Bean
	public ProducerFactory<String, MemberEventDto> producerFactoryForMemberEvent(){
		return new DefaultKafkaProducerFactory<>(producerConfigurations());
	}

	@Bean
	public KafkaTemplate<String, MemberEventDto> kafkaTemplateForMemberEvent(){
		return new KafkaTemplate<>(new DefaultKafkaProducerFactory<>(producerConfigurations()));
	}
}
