package com.bbebig.chatserver.domain.kafka.config;

import com.bbebig.chatserver.domain.kafka.dto.ChatMessageDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
@Slf4j
public class KafkaDmChatEventConsumerConfig {

	@Value("${spring.kafka.bootstrap-servers}")
	private String bootstrapAddress;

	@Value("${spring.kafka.consumer.group-id.dm-chat-event}")
	private String baseGroupId;

	@Value(("${eureka.instance.instance-id}"))
	private String instanceId;

	private Map<String, Object> consumerConfigurations() {
		String groupId = baseGroupId + "-" + instanceId;
		Map<String, Object> configurations = new HashMap<>();
		configurations.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
		configurations.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
		configurations.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		configurations.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,JsonDeserializer.class);
		configurations.put(JsonDeserializer.TRUSTED_PACKAGES,"*");
		configurations.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,"latest"); // earliest: 전체 , latest: 최신 메시지
		return configurations;
	}

	// 채팅 관련 Consumer 생성
	@Bean
	public ConsumerFactory<String, ChatMessageDto> consumerFactory(){
		return new DefaultKafkaConsumerFactory<>(consumerConfigurations(), new StringDeserializer(),
				new JsonDeserializer<>(ChatMessageDto.class));
	}

	// 멀티쓰레드에 대한 동기화 제공하는 컨슈머를 생산하기 위한 Factory
	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, ChatMessageDto> dmChatListener(){
		ConcurrentKafkaListenerContainerFactory<String, ChatMessageDto> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(consumerFactory());
		factory.setConcurrency(1); // 쓰레드 개수
		return factory;
	}


}
