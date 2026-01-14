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

    private final SumService sumService;
    private final OrderRepository orderRepository;

    public OrderService(SumService sumService, OrderRepository orderRepository) {
        this.sumService = sumService;
        this.orderRepository = orderRepository;
    }

    public List<OrderDto> getOrders() {
        List<Order> orders = orderRepository.findAll();

        return orders.stream().map(order -> {
            List<OrderItemDto> itemDtoList = order.getOrderItems().stream().map(OrderItemDtoMapper::mapp).toList();
            return new OrderDto(order.getId(), itemDtoList, sumService.calculateSum(itemDtoList));
        }).toList();
    }

    public OrderDto find(Long id) {
        Optional<Order> orderOptional = orderRepository.findById(id);
        if (orderOptional.isEmpty()) throw new NoSuchElementException();

        Order order = orderOptional.get();
        List<OrderItemDto> itemDtoList = order.getOrderItems().stream().map(OrderItemDtoMapper::mapp).toList();

        return new OrderDto(order.getId(), itemDtoList, sumService.calculateSum(itemDtoList));
    }

}
