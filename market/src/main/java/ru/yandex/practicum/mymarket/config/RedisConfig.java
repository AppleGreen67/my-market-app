package ru.yandex.practicum.mymarket.config;

import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import ru.yandex.practicum.mymarket.dto.ItemDto;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

@Configuration
public class RedisConfig {

    @Bean
    public RedisCacheManagerBuilderCustomizer itemCacheCustomizer() {
        return builder -> builder.withCacheConfiguration(
                "item",                                         // Имя кеша
                RedisCacheConfiguration.defaultCacheConfig()
//                        .entryTtl(Duration.of(1, ChronoUnit.MINUTES))  // TTL
                        .serializeValuesWith(                          // Сериализация JSON
                                RedisSerializationContext.SerializationPair.fromSerializer(
                                        new Jackson2JsonRedisSerializer<>(ItemDto.class)
                                )
                        )
        );
    }
}
