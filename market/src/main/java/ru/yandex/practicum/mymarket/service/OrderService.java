package ru.yandex.practicum.mymarket.service;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.yandex.practicum.mymarket.domain.Order;
import ru.yandex.practicum.mymarket.dto.ItemDto;
import ru.yandex.practicum.mymarket.dto.OrderDto;
import ru.yandex.practicum.mymarket.repository.OrderDatabaseClientRepository;
import ru.yandex.practicum.mymarket.repository.OrderRepository;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class OrderService {

    private final OrderRepository orderCrudRepository;
    private final OrderDatabaseClientRepository orderRepository;

    public OrderService(OrderRepository orderCrudRepository, OrderDatabaseClientRepository orderRepository) {
        this.orderCrudRepository = orderCrudRepository;
        this.orderRepository = orderRepository;
    }

    public Flux<OrderDto> getOrders(Long userId) {
        return orderRepository.findAll(userId);
    }

    public Mono<OrderDto> find(Long userId, Long orderId) {
        return orderRepository.findById(userId, orderId)
                .switchIfEmpty(Mono.error(new NoSuchElementException("Заказ не найден: " + orderId)));
    }

    public Mono<Long> create(Long userId) {
        Order order = new Order();
        order.setUserId(userId);
        return orderCrudRepository.save(order).map(Order::getId);
    }

    public Mono<Long> saveItems(List<ItemDto> cartItemsList, Long orderId) {
        return orderRepository.saveOrderItems(cartItemsList, orderId);
    }
}
