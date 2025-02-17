package com.bbebig.commonmodule.redis.config;

import com.bbebig.commonmodule.redis.domain.MemberPresenceStatus;
import com.bbebig.commonmodule.redis.domain.ServerMemberStatus;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

	/**
	 * 1) Long 전용 Set 저장용 템플릿
	 */
	@Bean
	public RedisTemplate<String, Long> redisSetTemplate(RedisConnectionFactory connectionFactory) {
		RedisTemplate<String, Long> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(connectionFactory);

		// Key: 문자열, Value: 문자열 (Long을 String으로 저장할 수도 있음)
		// -> 여기서는 Set에 Long을 직접 저장할 수 있도록
		//    ValueSerializer를 StringRedisSerializer로 설정 (단순화)
		redisTemplate.setKeySerializer(new StringRedisSerializer());
		redisTemplate.setValueSerializer(new StringRedisSerializer());

		return redisTemplate;
	}

	/**
	 * 2) MemberPresenceStatus JSON 직렬화용 템플릿
	 */
	@Bean
	public RedisTemplate<String, MemberPresenceStatus> redisMemberStatusTemplate(RedisConnectionFactory connectionFactory) {
		RedisTemplate<String, MemberPresenceStatus> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(connectionFactory);

		ObjectMapper objectMapper = createObjectMapper();

		redisTemplate.setKeySerializer(new StringRedisSerializer());
		redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer(objectMapper));

		return redisTemplate;
	}

	/**
	 * 3) ServerMemberStatus JSON 직렬화용 템플릿
	 */
	@Bean
	public RedisTemplate<String, ServerMemberStatus> redisServerStatusTemplate(RedisConnectionFactory connectionFactory) {
		RedisTemplate<String, ServerMemberStatus> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(connectionFactory);

		ObjectMapper objectMapper = createObjectMapper();

		redisTemplate.setKeySerializer(new StringRedisSerializer());
		redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer(objectMapper));

		return redisTemplate;
	}

	/**
	 * Jackson ObjectMapper 생성 (LocalDateTime, 타입 정보)
	 */
	private ObjectMapper createObjectMapper() {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		objectMapper.activateDefaultTyping(
				objectMapper.getPolymorphicTypeValidator(),
				ObjectMapper.DefaultTyping.NON_FINAL,
				JsonTypeInfo.As.PROPERTY
		);
		return objectMapper;
	}
}
