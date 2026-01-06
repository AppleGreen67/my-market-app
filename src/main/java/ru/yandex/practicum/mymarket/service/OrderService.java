package ru.yandex.practicum.mymarket.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.mymarket.domain.Order;
import ru.yandex.practicum.mymarket.dto.OrderDto;
import ru.yandex.practicum.mymarket.dto.OrderItemDto;
import ru.yandex.practicum.mymarket.mapper.OrderItemDtoMapper;
import ru.yandex.practicum.mymarket.repository.OrderRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public List<OrderDto> getOrders() {
        List<Order> orders = orderRepository.findAll();

        return orders.stream().map(order -> {
            List<OrderItemDto> itemDtoList = order.getOrderItems().stream().map(OrderItemDtoMapper::mapp).toList();
            return new OrderDto(order.getId(), itemDtoList, calculateSum(itemDtoList));
        }).toList();
    }

    public OrderDto find(Long id) {
        Optional<Order> orderOptional = orderRepository.findById(id);
        if (orderOptional.isEmpty()) throw new NoSuchElementException();

        Order order = orderOptional.get();
        List<OrderItemDto> itemDtoList = order.getOrderItems().stream().map(OrderItemDtoMapper::mapp).toList();

        return new OrderDto(order.getId(), itemDtoList, calculateSum(itemDtoList));
    }

    public Long calculateSum(List<OrderItemDto> items) {
        if (items == null || items.isEmpty()) return 0L;

        return items.stream().mapToLong(item -> item.price() * item.count()).sum();
    }
}
