package ru.yandex.practicum.mymarket.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.yandex.practicum.mymarket.domain.CartItem;
import ru.yandex.practicum.mymarket.dto.ItemDto;
import ru.yandex.practicum.mymarket.repository.CartDatabaseClientRepository;
import ru.yandex.practicum.mymarket.repository.ItemDatabaseClientRepository;
import ru.yandex.practicum.mymarket.utils.ListUtils;

import java.util.List;

@Service
public class ItemsService {

    private final CartService cartService;
    private final ItemDatabaseClientRepository itemRepository;
    private final CartDatabaseClientRepository cartRepository;

    public ItemsService(CartService cartService, ItemDatabaseClientRepository itemRepository, CartDatabaseClientRepository cartRepository) {
        this.cartService = cartService;
        this.itemRepository = itemRepository;
        this.cartRepository = cartRepository;
    }

    @Transactional
    public Mono<List<List<ItemDto>>> getItems(Long userId, String search, String sort, Integer pageNumber, Integer pageSize) {
        return itemRepository.findAll(search, sort, pageNumber, pageSize)
                .collectList()
                .flatMapMany(itemsList -> {
                    List<Long> ids = itemsList.stream().map(ItemDto::getId).toList();
                    return cartRepository.findItems(userId, ids)
                            .collectMap(CartItem::getItemId, CartItem::getCount)
                            .flatMapMany(countMap ->
                                    Flux.fromIterable(itemsList)
                                            .map(itemDto -> {
                                                itemDto.setCount(countMap.get(itemDto.getId()) == null ? 0 : countMap.get(itemDto.getId()));
                                                return itemDto;
                                            })
                            );
                })
                .collectList()
                .map(list -> ListUtils.partition(list, 3));

    }

    public Mono<Long> updateCountInCart(Long id, String action, Long userId) {
        return cartService.updateCart(id, action, userId);
    }

    @Transactional
    public Mono<ItemDto> find(Long id, Long userId) {
        return Mono.zip(itemRepository.findById(id), cartRepository.findItem(userId, id))
                .map(tuple -> {
                    ItemDto itemDto = tuple.getT1();
                    CartItem cartItem = tuple.getT2();
                    if (cartItem.getCount()!= null)
                        itemDto.setCount(cartItem.getCount());
                    return itemDto;
                });
    }
}
