package com.bbebig.commonmodule.kafka.config;

import com.bbebig.commonmodule.kafka.dto.ChatMessageDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.ConsumerAwareRebalanceListener;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.util.backoff.FixedBackOff;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
@Slf4j
public class KafkaChannelChatEventConsumerConfig {

	@Value("${spring.kafka.bootstrap-servers}")
	private String bootstrapAddress;

	@Value("${spring.kafka.consumer.group-id.channel-chat-event}")
	private String baseGroupId;

	@Value("${eureka.instance.instance-id}")
	private String instanceId;

	// Producer side (for DLT)
	@Bean
	public Map<String, Object> producerConfigs() {
		Map<String, Object> props = new HashMap<>();
		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, org.apache.kafka.common.serialization.StringSerializer.class);
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, org.springframework.kafka.support.serializer.JsonSerializer.class);
		props.put(ProducerConfig.ACKS_CONFIG, "all");
		return props;
	}

	@Bean
	public ProducerFactory<String, Object> producerFactorForChannelDLQ() {
		return new DefaultKafkaProducerFactory<>(producerConfigs());
	}

	@Bean
	public KafkaTemplate<String, Object> kafkaTemplateForChannelDLQ() {
		return new KafkaTemplate<>(producerFactorForChannelDLQ());
	}

	@Bean
	public DeadLetterPublishingRecoverer deadLetterPublishingRecoverer(KafkaTemplate<String, Object> kafkaTemplate) {
		return new DeadLetterPublishingRecoverer(kafkaTemplate);
	}

	@Bean
	public DefaultErrorHandler defaultErrorHandler(DeadLetterPublishingRecoverer recoverer) {
		FixedBackOff backOff = new FixedBackOff(1000L, 3L);
		DefaultErrorHandler errorHandler = new DefaultErrorHandler(recoverer, backOff);
		return errorHandler;
	}

	//Consumer side

	private Map<String, Object> consumerConfigurations() {
		String groupId = baseGroupId + "-" + instanceId;
		Map<String, Object> configurations = new HashMap<>();
		configurations.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
		configurations.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
		configurations.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		configurations.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
		configurations.put(JsonDeserializer.TRUSTED_PACKAGES, "*");

		return configurations;
	}

	@Bean
	public ConsumerFactory<String, ChatMessageDto> consumerFactoryForChannelChatEvent(){
		return new DefaultKafkaConsumerFactory<>(
				consumerConfigurations(),
				new StringDeserializer(),
				new JsonDeserializer<>(ChatMessageDto.class)
		);
	}

	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, ChatMessageDto> channelChatListener(
			DefaultErrorHandler errorHandler
	){
		ConcurrentKafkaListenerContainerFactory<String, ChatMessageDto> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(consumerFactoryForChannelChatEvent());
		factory.setConcurrency(3);
		factory.setBatchListener(true);
		factory.setCommonErrorHandler(errorHandler);

		ContainerProperties props = factory.getContainerProperties();
		props.setConsumerRebalanceListener(rebalanceListener());
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
