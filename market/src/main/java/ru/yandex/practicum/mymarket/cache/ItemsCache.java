package ru.yandex.practicum.mymarket.cache;

import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import ru.yandex.practicum.mymarket.dto.ItemDto;
import ru.yandex.practicum.mymarket.repository.ItemDatabaseClientRepository;

import java.time.Duration;

@Service
public class ItemsCache {

    private final ItemDatabaseClientRepository itemRepository;
    private final ReactiveRedisTemplate<String, ItemDto> redisTemplate;

    private static final Duration CACHE_TTL = Duration.ofMinutes(1);

    public ItemsCache(ItemDatabaseClientRepository itemRepository, ReactiveRedisTemplate<String, ItemDto> redisTemplate) {
        this.itemRepository = itemRepository;
        this.redisTemplate = redisTemplate;
    }

    public Flux<ItemDto> findAll(Long userId, String search, String sort, Integer pageNumber, Integer pageSize) {
        String key = createKey(search, sort, pageNumber, pageSize);
        return redisTemplate.opsForList()
                .range(key, 0, -1)
                .collectList()
                .flatMapMany(cachedItems -> {
                    if (cachedItems != null && !cachedItems.isEmpty()) {
                        System.out.println("Found items in cache");
                        return Flux.fromIterable(cachedItems);
                    }

                    System.out.println("NO items in cache");
                    return findInDBAndSaveInCache(key, search, sort, pageNumber, pageSize);
                });
    }

    private Flux<ItemDto> findInDBAndSaveInCache(String key, String search, String sort, Integer pageNumber, Integer pageSize) {
        return itemRepository.findAll(search, sort, pageNumber, pageSize)
                .collectList()
                .flatMapMany(itemDtoList -> {
                    if (itemDtoList.isEmpty())
                        return Flux.empty();

                    return redisTemplate.opsForList()
                            .rightPushAll(key, itemDtoList)
                            .then(redisTemplate.expire(key, CACHE_TTL))
                            .thenMany(Flux.fromIterable(itemDtoList));
                });
    }

    private String createKey(String search, String sort, Integer pageNumber, Integer pageSize) {
        return new StringBuilder()
                .append("items:")
                .append(search == null ? "" : search)
                .append(":")
                .append(sort)
                .append(":")
                .append(pageNumber)
                .append(":")
                .append(pageSize)
                .append(":").toString();
    }
}
