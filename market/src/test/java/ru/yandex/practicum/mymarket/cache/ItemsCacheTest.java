package ru.yandex.practicum.mymarket.cache;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.context.ImportTestcontainers;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.testcontainers.junit.jupiter.Testcontainers;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import ru.yandex.practicum.mymarket.dto.ItemDto;
import ru.yandex.practicum.mymarket.redis.RedisTestContainer;
import ru.yandex.practicum.mymarket.repository.ItemDatabaseClientRepository;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static ru.yandex.practicum.mymarket.utils.KeyUtils.createKey;

@SpringBootTest
@Testcontainers
@ImportTestcontainers(RedisTestContainer.class)
class ItemsCacheTest {

    @MockitoBean
    private ItemDatabaseClientRepository itemRepository;

    @Autowired
    private ReactiveRedisTemplate<String, ItemDto> redisTemplate;

    @Autowired
    private ItemsCache itemsCache;

    @Test
    void findAll() {
        ItemDto itemDto = new ItemDto(1L, "title1", "description1", "imageUrl", 11L, 111);
        Flux<ItemDto> items = Flux.just(itemDto,
                new ItemDto(3L, "title3", "description3", "imageUrl", 33L, 333));

        when(itemRepository.findAll(any(), any(), any(), any())).thenReturn(items);

        StepVerifier.create(itemsCache.findAll("", "NO", 1, 5))
                .expectNextCount(2)
                .verifyComplete();

        StepVerifier.create(redisTemplate.opsForList()
                        .range(createKey("", "NO", 1, 5), 0, -1)
                        .count()
                )
                .expectNext(2L)
                .verifyComplete();

        StepVerifier.create(itemsCache.findAll("", "NO", 1, 5))
                .expectNextCount(2)
                .verifyComplete();

        verify(itemRepository, times(1)).findAll(any(), any(), any(), any());
    }
}