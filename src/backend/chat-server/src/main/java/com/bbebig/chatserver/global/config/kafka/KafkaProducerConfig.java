package com.bbebig.chatserver.global.config.kafka;

import com.bbebig.chatserver.domain.dto.ChatMessageDto;
import com.bbebig.chatserver.domain.dto.ConnectionEventDto;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ser.std.StringSerializer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

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
	public ProducerFactory<String, ChatMessageDto> producerFactoryForChat(){
		return new DefaultKafkaProducerFactory<>(producerConfigurations());
	}

	@Bean
	public KafkaTemplate<String, ChatMessageDto> kafkaTemplateForChat(){
		return new KafkaTemplate<>(producerFactoryForChat());
	}

	// 세션 이벤트(웹소켓 연결) Producer
	@Bean
	public ProducerFactory<String, ConnectionEventDto> producerFactoryForSession(){
		return new DefaultKafkaProducerFactory<>(producerConfigurations());
	}

	@Bean
	public KafkaTemplate<String, ConnectionEventDto> kafkaTemplateForSession(){
		return new KafkaTemplate<>(producerFactoryForSession());
	}
}
