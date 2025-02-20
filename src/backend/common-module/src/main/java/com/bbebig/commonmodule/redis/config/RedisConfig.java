package com.bbebig.commonmodule.redis.config;

import com.bbebig.commonmodule.redis.domain.*;
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

	/**
	 * ObjectMapper를 Bean으로 등록하여 여러 RedisTemplate에서 재사용 가능
	 */
	@Bean
	public ObjectMapper objectMapper() {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		objectMapper.registerModule(new JavaTimeModule());
		return objectMapper;
	}

	/**
	 * JSON 직렬화 설정을 가진 RedisSerializer Bean 등록
	 */
	@Bean
	public Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer(ObjectMapper objectMapper) {
		return new Jackson2JsonRedisSerializer<>(Object.class);
	}

	/**
	 * 기본 RedisTemplate (Object 저장용) - 모든 일반 객체 저장 가능
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
		RedisTemplate<String, Long> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(connectionFactory);
		redisTemplate.setKeySerializer(new StringRedisSerializer());
		redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(Long.class)); // JSON 직렬화 적용
		return redisTemplate;
	}

	/**
	 * MemberPresenceStatus 전용 RedisTemplate
	 */
	@Bean
	public RedisTemplate<String, MemberPresenceStatus> redisMemberStatusTemplate(
			RedisConnectionFactory connectionFactory,
			ObjectMapper objectMapper) {
		return createTypedRedisTemplate(connectionFactory, objectMapper, MemberPresenceStatus.class);
	}

	/**
	 * ServerMemberStatus 전용 RedisTemplate
	 */
	@Bean
	public RedisTemplate<String, ServerMemberStatus> redisServerStatusTemplate(
			RedisConnectionFactory connectionFactory,
			ObjectMapper objectMapper) {
		return createTypedRedisTemplate(connectionFactory, objectMapper, ServerMemberStatus.class);
	}

	/**
	 * ServerLastInfo 전용 RedisTemplate
	 */
	@Bean
	public RedisTemplate<String, ServerLastInfo> redisServerLastInfoTemplate(
			RedisConnectionFactory connectionFactory,
			ObjectMapper objectMapper) {
		return createTypedRedisTemplate(connectionFactory, objectMapper, ServerLastInfo.class);
	}

	/**
	 * 공통 RedisTemplate 생성 메서드 (Object 직렬화)
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
	 * 특정 타입을 위한 RedisTemplate 생성 (제네릭 지원)
	 */
	private <T> RedisTemplate<String, T> createTypedRedisTemplate(
			RedisConnectionFactory connectionFactory,
			ObjectMapper objectMapper,
			Class<T> clazz) {
		RedisTemplate<String, T> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(connectionFactory);
		redisTemplate.setKeySerializer(new StringRedisSerializer());
		redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(clazz)); // 특정 클래스 타입 직렬화
		redisTemplate.setHashKeySerializer(new StringRedisSerializer());
		redisTemplate.setHashValueSerializer(new Jackson2JsonRedisSerializer<>(clazz));
		return redisTemplate;
	}
}
