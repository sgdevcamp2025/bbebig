package com.bbebig.commonmodule.redis.config;

import com.bbebig.commonmodule.redis.domain.*;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

	private ObjectMapper createRedisObjectMapper() {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		objectMapper.registerModule(new JavaTimeModule());

		// 타입 정보 활성화 (Object 직렬화 지원)
		objectMapper.activateDefaultTyping(
				objectMapper.getPolymorphicTypeValidator(),
				ObjectMapper.DefaultTyping.NON_FINAL,
				JsonTypeInfo.As.PROPERTY
		);
		return objectMapper;
	}

	/**
	 * Redis JSON 직렬화기
	 * - Redis에서 사용할 `ObjectMapper` 적용
	 */
	@Bean
	public Jackson2JsonRedisSerializer<Object> redisJsonSerializer() {
		ObjectMapper redisObjectMapper = createRedisObjectMapper();
		return new Jackson2JsonRedisSerializer<>(redisObjectMapper, Object.class);
	}

	/**
	 * 기본 RedisTemplate (Object 저장용)
	 */
	@Bean
	public RedisTemplate<String, Object> redisTemplate(
			RedisConnectionFactory connectionFactory,
			Jackson2JsonRedisSerializer<Object> serializer) {
		return createRedisTemplate(connectionFactory, serializer);
	}

	/**
	 * Long 타입을 저장하는 RedisTemplate (Set 데이터 저장용)
	 */
	@Bean
	public RedisTemplate<String, Long> redisSetTemplate(RedisConnectionFactory connectionFactory) {
		ObjectMapper redisObjectMapper = createRedisObjectMapper();
		Jackson2JsonRedisSerializer<Long> serializer = new Jackson2JsonRedisSerializer<>(redisObjectMapper, Long.class); // ✅ ObjectMapper 적용

		RedisTemplate<String, Long> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(connectionFactory);
		redisTemplate.setKeySerializer(new StringRedisSerializer());
		redisTemplate.setValueSerializer(serializer);
		redisTemplate.setHashKeySerializer(new StringRedisSerializer());
		redisTemplate.setHashValueSerializer(serializer);
		return redisTemplate;
	}

	/**
	 * **MemberPresenceStatus 전용 RedisTemplate**
	 */
	@Bean
	public RedisTemplate<String, MemberPresenceStatus> redisMemberStatusTemplate(
			RedisConnectionFactory connectionFactory) {
		return createTypedRedisTemplate(connectionFactory, MemberPresenceStatus.class);
	}

	/**
	 * ServerMemberStatus 전용 RedisTemplate
	 */
	@Bean
	public RedisTemplate<String, ServerMemberStatus> redisServerStatusTemplate(
			RedisConnectionFactory connectionFactory) {
		return createTypedRedisTemplate(connectionFactory, ServerMemberStatus.class);
	}

	/**
	 * ServerLastInfo 전용 RedisTemplate
	 */
	@Bean
	public RedisTemplate<String, ServerLastInfo> redisServerLastInfoTemplate(
			RedisConnectionFactory connectionFactory) {
		return createTypedRedisTemplate(connectionFactory, ServerLastInfo.class);
	}

	/**
	 * 공통 RedisTemplate 생성 메서드
	 */
	private <T> RedisTemplate<String, T> createRedisTemplate(
			RedisConnectionFactory connectionFactory,
			Jackson2JsonRedisSerializer<T> serializer) {
		RedisTemplate<String, T> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(connectionFactory);
		redisTemplate.setKeySerializer(new StringRedisSerializer());
		redisTemplate.setValueSerializer(serializer);
		redisTemplate.setHashKeySerializer(new StringRedisSerializer());
		redisTemplate.setHashValueSerializer(serializer);
		return redisTemplate;
	}

	/**
	 * 특정 타입을 위한 RedisTemplate 생성
	 */
	private <T> RedisTemplate<String, T> createTypedRedisTemplate(
			RedisConnectionFactory connectionFactory,
			Class<T> clazz) {
		ObjectMapper redisObjectMapper = createRedisObjectMapper();
		Jackson2JsonRedisSerializer<T> serializer = new Jackson2JsonRedisSerializer<>(redisObjectMapper, clazz); // ✅ ObjectMapper 적용

		RedisTemplate<String, T> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(connectionFactory);
		redisTemplate.setKeySerializer(new StringRedisSerializer());
		redisTemplate.setValueSerializer(serializer);
		redisTemplate.setHashKeySerializer(new StringRedisSerializer());
		redisTemplate.setHashValueSerializer(serializer);
		return redisTemplate;
	}
}
