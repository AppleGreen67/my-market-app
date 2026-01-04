package ru.yandex.practicum.mymarket.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.mymarket.dto.Item;
import ru.yandex.practicum.mymarket.dto.Order;

import java.util.Arrays;
import java.util.List;

@Service
public class OrderService {
    public List<Order> getOrders() {
        List<Item> items = Arrays.asList(new Item(1L, "title", "description", "imageUrl", 0L, 0L),
                new Item(2L, "title1", "description1", "imageUrl", 0L, 0L),
                new Item(2L, "title2", "description2", "imageUrl", 0L, 0L));
        return Arrays.asList(new Order(1L, items, 8907L));
    }

    public Order find(Long id) {
        List<Item> items = Arrays.asList(new Item(1L, "title", "description", "imageUrl", 0L, 0L),
                new Item(2L, "title1", "description1", "imageUrl", 0L, 0L),
                new Item(2L, "title2", "description2", "imageUrl", 0L, 0L));

        return new Order(1L, items, 8907L);
    }
}
