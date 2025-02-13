package com.bbebig.commonmodule.kafka.config;

import com.bbebig.commonmodule.kafka.dto.ChannelEventDto;
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
public class KafkaChannelEventConsumerConfig {

	@Value("${spring.kafka.bootstrap-servers}")
	private String bootstrapAddress;

	@Value("${spring.kafka.consumer.group-id.channel-event}")
	private String baseGroupId;

	@Value("${eureka.instance.instance-id}")
	private String instanceId;

	private Map<String, Object> consumerConfigurations() {
		String groupId = baseGroupId + "-" + instanceId;
		Map<String, Object> configurations = new HashMap<>();
		configurations.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
		configurations.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
		configurations.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		configurations.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
		configurations.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
		configurations.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest"); // 최신 메시지
		return configurations;
	}

	@Bean
	public ConsumerFactory<String, ChannelEventDto> consumerFactoryForChannelEvent() {
		return new DefaultKafkaConsumerFactory<>(consumerConfigurations(), new StringDeserializer(),
				new JsonDeserializer<>(ChannelEventDto.class));
	}

	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, ChannelEventDto> channelEventListener() {
		ConcurrentKafkaListenerContainerFactory<String, ChannelEventDto> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(consumerFactoryForChannelEvent());
		factory.setConcurrency(1); // 단일 컨슈머 설정
		return factory;
	}

}
