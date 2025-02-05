package com.bbebig.chatserver.global.config;

import com.bbebig.chatserver.domain.chat.service.TypingEventRedisSubscriber;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

@Configuration
@EnableRedisRepositories
public class RedisPubSubConfig {
	/*
	 Redis Pub/Sub 메시지 리스너 설정
	*/
	@Bean
	public RedisMessageListenerContainer redisContainer(RedisConnectionFactory connectionFactory,
														MessageListenerAdapter listenerAdapter) {
		RedisMessageListenerContainer container = new RedisMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);

		// Redis Pub/Sub 채널 구독
		container.addMessageListener(listenerAdapter, typingEventTopic());
		return container;
	}

	/*
	 RedisSubscriber에서 메시지를 수신하도록 설정
	*/
	@Bean
	public MessageListenerAdapter listenerAdapter(TypingEventRedisSubscriber subscriber) {
		MessageListenerAdapter adapter = new MessageListenerAdapter(subscriber, "onMessage");
		adapter.setSerializer(new Jackson2JsonRedisSerializer<>(String.class)); // JSON 직렬화 설정
		return adapter;
	}

	/*
	 Redis Pub/Sub 채널 설정
	*/
	@Bean
	public ChannelTopic typingEventTopic() {
		return new ChannelTopic("typingEvent");
	}

}
