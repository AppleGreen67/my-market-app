package ru.yandex.practicum.mymarket.service;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import ru.yandex.practicum.mymarket.dto.ItemDto;
import ru.yandex.practicum.mymarket.repository.ItemDatabaseClientRepository;
import ru.yandex.practicum.mymarket.utils.ListUtils;

import java.util.List;


@Service
public class ItemsService {

    private final CartService cartService;
    private final ItemDatabaseClientRepository itemRepository;

    public ItemsService(CartService cartService, ItemDatabaseClientRepository itemRepository) {
        this.cartService = cartService;
        this.itemRepository = itemRepository;
    }

    @Transactional
    public Mono<List<List<ItemDto>>> getItems(Long userId, String search, String sort, Integer pageNumber, Integer pageSize) {
        return itemRepository.findAll(search, sort, pageNumber, pageSize).collectList()
                .map(list -> ListUtils.partition(list, 3));
    }

    public Mono<ItemDto> updateCountInCart(Long itemId, String action, Long userId) {
        return cartService.updateCart(itemId, action, userId)
                .filter(itemDto -> itemId.equals(itemDto.getId())).next();
    }

    @Transactional
    @Cacheable(value = "item", key = "#id")
    public Mono<ItemDto> find(Long id, Long userId) {
        System.out.println("Читаю из репозитория для id=" + id);
        return itemRepository.findById(id);
    }
}
