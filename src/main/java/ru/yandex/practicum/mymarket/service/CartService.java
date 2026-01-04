package ru.yandex.practicum.mymarket.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.mymarket.dto.Item;

import java.util.Arrays;
import java.util.List;

@Service
public class CartService {

    public List<Item> getItems() {
        return Arrays.asList(new Item(1L, "title", "description", "imageUrl", 0L, 0L),
                new Item(2L, "title1", "description1", "imageUrl", 0L, 0L),
                new Item(2L, "title2", "description2", "imageUrl", 0L, 0L));
    }

    public List<Item> updateCount(String id, String action) {
        return Arrays.asList(new Item(1L, "title", "description", "imageUrl", 0L, 0L),
                new Item(2L, "title2", "description2", "imageUrl", 0L, 0L));
    }
}
