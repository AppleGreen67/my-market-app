package ru.yandex.practicum.mymarket.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import ru.yandex.practicum.mymarket.domain.Order;
import ru.yandex.practicum.mymarket.domain.OrderItem;
import ru.yandex.practicum.mymarket.repository.CartRepository;
import ru.yandex.practicum.mymarket.repository.OrderRepository;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class BuyService {

    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;

    public BuyService(CartRepository cartRepository, OrderRepository orderRepository) {
        this.cartRepository = cartRepository;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public Mono<Long> buy(Long userId) {
        return cartRepository.findByUserId(userId)
                .switchIfEmpty(Mono.error(new NoSuchElementException("Корзина не найдена для пользователя: " + userId)))
                .flatMap(cart ->{
                    Order order = new Order();

                    List<OrderItem> orderItems = cart.getItems().stream()
                            .map(cartItem -> {
                                OrderItem orderItem = new OrderItem();
                                orderItem.setItem(cartItem.getItem());
                                orderItem.setCount(cartItem.getCount());
                                orderItem.setOrder(order);
                                return orderItem;
                            }).toList();

                    order.setOrderItems(orderItems);

                    return orderRepository.save(order)
                            .flatMap(savedOrder -> {
                                cart.getItems().clear();
                                return cartRepository.save(cart).thenReturn(savedOrder.getId());
                            });
                });
    }
}
