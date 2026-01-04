package ru.yandex.practicum.mymarket.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.mymarket.dto.Item;

import java.util.Arrays;
import java.util.List;

@Service
public class ItemsService {


    public List<List<Item>> getItems(String search, String sort, String pageNumber, String pageSize) {
        List<Item> items = Arrays.asList(new Item(1L, "title", "description", "imageUrl", 0L, 0L),
                new Item(2L, "title1", "description1", "imageUrl", 0L, 0L),
                new Item(2L, "title2", "description2", "imageUrl", 0L, 0L));
        return Arrays.asList(items, items);
    }

    public Item updateCount(Long id, String action) {
        return new Item(2L, "title1", "description1", "imageUrl", 0L, 0L);
    }

    public Item find(Long id) {
        return new Item(2L, "title1", "description1", "imageUrl", 0L, 0L);
    }

}
