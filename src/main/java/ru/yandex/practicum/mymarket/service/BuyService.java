package ru.yandex.practicum.mymarket.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.NoSuchElementException;

@Service
public class BuyService {

    private final CartService cartService;
    private final OrderService orderService;

    public BuyService(CartService cartService, OrderService orderService) {
        this.cartService = cartService;
        this.orderService = orderService;
    }

    @Transactional
    public Mono<Long> buy(Long userId) {
        return cartService.getCartItems(userId)
                .switchIfEmpty(Mono.error(new NoSuchElementException("Корзина не найдена для пользователя: " + userId)))
                .collectList()
                .flatMap(cartItemsList ->
                        orderService.create(userId)
                                .flatMap(orderId -> orderService.saveItems(cartItemsList, orderId)
                                        .flatMap(l -> cartService.deleteAll(userId))
                                        .then(Mono.just(orderId))));
    }
}
