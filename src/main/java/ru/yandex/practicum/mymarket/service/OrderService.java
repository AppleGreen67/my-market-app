package ru.yandex.practicum.mymarket.service;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.yandex.practicum.mymarket.dto.OrderDto;
import ru.yandex.practicum.mymarket.dto.OrderItemDto;
import ru.yandex.practicum.mymarket.mapper.OrderItemDtoMapper;
import ru.yandex.practicum.mymarket.repository.OrderRepository;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class OrderService {

    private final SumService sumService;
    private final OrderRepository orderRepository;

    public OrderService(SumService sumService, OrderRepository orderRepository) {
        this.sumService = sumService;
        this.orderRepository = orderRepository;
    }

    public Flux<OrderDto> getOrders() {
        return orderRepository.findAll()
                .map(order -> {
                    List<OrderItemDto> list = order.getOrderItems().stream().map(OrderItemDtoMapper::mapp).toList();
                    return new OrderDto(order.getId(), list, sumService.calculateSum(list));
                });
    }

    public Mono<OrderDto> find(Long id) {
        return orderRepository.findById(id)
                .switchIfEmpty(Mono.error(new NoSuchElementException("Заказ не найден: " + id)))
                .map(order -> {
                    List<OrderItemDto> list = order.getOrderItems().stream().map(OrderItemDtoMapper::mapp).toList();
                    return new OrderDto(order.getId(), list, sumService.calculateSum(list));
                });
    }

}
