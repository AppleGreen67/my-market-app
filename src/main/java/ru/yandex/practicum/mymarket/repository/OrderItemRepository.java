package ru.yandex.practicum.mymarket.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import ru.yandex.practicum.mymarket.domain.OrderItem;

import java.util.Collection;

public interface OrderItemRepository extends ReactiveCrudRepository<OrderItem, Long> {

    Flux<OrderItem> findByOrderId(Long orderId);
    Flux<OrderItem> findByOrderIdIn(Collection<Long> orderIds);

    Flux<OrderItem> findAllOrderItemByOrderId(Long orderId);
}
