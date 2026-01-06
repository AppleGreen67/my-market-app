package ru.yandex.practicum.mymarket.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.mymarket.domain.Item;
import ru.yandex.practicum.mymarket.domain.Order;
import ru.yandex.practicum.mymarket.domain.OrderItem;
import ru.yandex.practicum.mymarket.repository.ItemRepository;
import ru.yandex.practicum.mymarket.repository.OrderRepository;

import java.util.List;

@Service
public class BuyService {

    private final ItemRepository itemRepository;
    private final OrderRepository orderRepository;

    public BuyService(ItemRepository itemRepository, OrderRepository orderRepository) {
        this.itemRepository = itemRepository;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public Long buy() {
        List<Item> itemsInCart = itemRepository.findByCountGreaterThan(0);

        Order order = new Order();

        List<OrderItem> orderItems = itemsInCart.stream()
                .map(item -> {
                    OrderItem orderItem = new OrderItem();
                    orderItem.setItem(item);
                    orderItem.setCount(item.getCount());
                    orderItem.setOrder(order);
                    return orderItem;
                }).toList();

        order.setOrderItems(orderItems);
        orderRepository.save(order);

        itemsInCart.forEach(item -> item.setCount(0));
        itemRepository.saveAll(itemsInCart);

        return order.getId();
    }
}
