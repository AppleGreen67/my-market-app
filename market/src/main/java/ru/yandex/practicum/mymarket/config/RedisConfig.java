package ru.yandex.practicum.mymarket.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;
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
                        .entryTtl(Duration.of(1, ChronoUnit.MINUTES))  // TTL
                        .serializeValuesWith(                          // Сериализация JSON
                                RedisSerializationContext.SerializationPair.fromSerializer(
                                        new Jackson2JsonRedisSerializer<>(ItemDto.class)
                                )
                        )
        );
    }

    @Bean
    public ReactiveRedisTemplate<String, ItemDto> reactiveRedisTemplate(ReactiveRedisConnectionFactory factory,
                                                                        ObjectMapper objectMapper) {

        Jackson2JsonRedisSerializer<ItemDto> serializer = new Jackson2JsonRedisSerializer<>(objectMapper, ItemDto.class);

        RedisSerializationContext<String, ItemDto> context =
                RedisSerializationContext.<String, ItemDto>newSerializationContext(new StringRedisSerializer())
                        .value(serializer)
                        .build();
        return new ReactiveRedisTemplate<>(factory, context);
    }
}
