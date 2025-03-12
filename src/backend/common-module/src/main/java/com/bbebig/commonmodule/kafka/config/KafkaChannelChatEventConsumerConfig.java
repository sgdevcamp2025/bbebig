package com.bbebig.commonmodule.kafka.config;

import com.bbebig.commonmodule.kafka.dto.ChatMessageDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConsumerAwareRebalanceListener;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
@Slf4j
public class KafkaChannelChatEventConsumerConfig {

	@Value("${spring.kafka.bootstrap-servers}")
	private String bootstrapAddress;

	@Value("${spring.kafka.consumer.group-id.channel-chat-event}")
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
	public ConsumerFactory<String, ChatMessageDto> consumerFactoryForChannelChatEvent(){
		return new DefaultKafkaConsumerFactory<>(consumerConfigurations(), new StringDeserializer(),
				new JsonDeserializer<>(ChatMessageDto.class));
	}

	// 멀티쓰레드에 대한 동기화 제공하는 컨슈머를 생산하기 위한 Factory
	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, ChatMessageDto> channelChatListener(){
		ConcurrentKafkaListenerContainerFactory<String, ChatMessageDto> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(consumerFactoryForChannelChatEvent());
		ContainerProperties prop = factory.getContainerProperties();
		prop.setConsumerRebalanceListener(rebalanceListener());
		factory.setConcurrency(3); // 쓰레드 개수
		factory.setBatchListener(true);
		return factory;
	}

	@Bean
	public ConsumerAwareRebalanceListener rebalanceListener() {
		return new ConsumerAwareRebalanceListener() {
			@Override
			public void onPartitionsAssigned(Consumer<?, ?> consumer, Collection<TopicPartition> partitions) {
				partitions.forEach(partition ->
					log.info("[Chat] 할당된 파티션: Topic: {}, Partition: {}", partition.topic(), partition.partition())
				);
			}
		};
	}


}
