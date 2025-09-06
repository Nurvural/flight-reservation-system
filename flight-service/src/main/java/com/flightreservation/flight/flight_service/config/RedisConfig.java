package com.flightreservation.flight.flight_service.config;

import java.time.Duration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Configuration
public class RedisConfig {

	@Bean
	public RedisCacheConfiguration cacheConfiguration() {
		ObjectMapper om = new ObjectMapper();
		om.registerModule(new JavaTimeModule());

		RedisSerializationContext.SerializationPair<Object> pair = RedisSerializationContext.SerializationPair
				.fromSerializer(new GenericJackson2JsonRedisSerializer(om));

		return RedisCacheConfiguration.defaultCacheConfig().serializeValuesWith(pair).entryTtl(Duration.ofMinutes(60));
	}

}
