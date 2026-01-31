package ru.yandex.practicum.mymarket.service;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.yandex.practicum.mymarket.dto.OrderDto;
import ru.yandex.practicum.mymarket.repository.OrderDatabaseClientRepository;

import java.util.NoSuchElementException;

@Service
public class OrderService {

    private final OrderDatabaseClientRepository orderRepository;

    public OrderService(OrderDatabaseClientRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Flux<OrderDto> getOrders(Long userId) {
        return orderRepository.findAll(userId);
    }

    public Mono<OrderDto> find(Long userId, Long orderId) {
        return orderRepository.findById(userId, orderId)
                .switchIfEmpty(Mono.error(new NoSuchElementException("Заказ не найден: " + orderId)));
    }

}
