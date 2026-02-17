package ru.yandex.practicum.mymarket.service;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.yandex.practicum.mymarket.cache.ItemsCache;
import ru.yandex.practicum.mymarket.domain.CartItem;
import ru.yandex.practicum.mymarket.dto.ItemDto;
import ru.yandex.practicum.mymarket.repository.CartDatabaseClientRepository;

import java.util.NoSuchElementException;

@Service
public class CartService {

    private final ItemsCache itemsCache;
    private final CartDatabaseClientRepository cartDatabaseClientRepository;

    public CartService(ItemsCache itemsCache, CartDatabaseClientRepository cartDatabaseClientRepository) {
        this.itemsCache = itemsCache;
        this.cartDatabaseClientRepository = cartDatabaseClientRepository;
    }

    public Flux<ItemDto> getCartItems(Long userId) {
        return cartDatabaseClientRepository.findByUserId(userId);
    }

    public Mono<Long> updateCart(Long itemId, String action, Long userId) {
        return cartDatabaseClientRepository.findByUserIdAndItemId(userId, itemId)
                .flatMap(cartItem -> {
                    Integer newCount = getCount(cartItem.getCount(), action);
                    if (newCount == 0)
                        return cartDatabaseClientRepository.deleteItem(userId, itemId);
                    else
                        return cartDatabaseClientRepository.updateItem(userId, itemId, newCount);
                })
                .switchIfEmpty(addItem(userId, itemId));
    }

    private Mono<Long> addItem(Long userId, Long itemId) {
        return itemsCache.findById(itemId)
                .map(item -> {
                    CartItem cartItem = new CartItem();
                    cartItem.setUserId(userId);
                    cartItem.setCount(1);
                    cartItem.setItemId(itemId);
                    return cartItem;
                })
                .switchIfEmpty((Mono.error(new NoSuchElementException("Не найдет товар в корзине с id=" + itemId))))
                .flatMap(cartDatabaseClientRepository::saveItem);
    }

    public Mono<Long> deleteAll(Long userId) {
        return cartDatabaseClientRepository.deleteAll(userId);
    }

    private Integer getCount(Integer count, String action) {
        if ("MINUS".equals(action))
            return count - 1;
        else if ("PLUS".equals(action))
            return count + 1;
        else if ("DELETE".equals(action))
            return 0;

        throw new UnsupportedOperationException();
    }

}
