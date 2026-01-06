package ru.yandex.practicum.mymarket.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.mymarket.domain.Item;
import ru.yandex.practicum.mymarket.dto.ItemDto;
import ru.yandex.practicum.mymarket.mapper.ItemMapper;
import ru.yandex.practicum.mymarket.repository.ItemRepository;

import java.util.List;

@Service
public class CartService extends ItemsService {

//    private final ItemRepository itemRepository;
//
//    public CartService(ItemRepository itemRepository) {
//        this.itemRepository = itemRepository;
//    }


    public CartService(ItemRepository itemRepository) {
        super(itemRepository);
    }

    public List<ItemDto> getItems() {
        List<Item> items = itemRepository.findByCountGreaterThan(0);
        return items.stream().map(ItemMapper::mapp).toList();
    }

    @Transactional
    public List<ItemDto> updateItemCountInCart(Long id, String action) {
        updateCount(id, action);
        return getItems();
    }

    public Long calculateSum(List<ItemDto> items) {
        if (items == null || items.isEmpty()) return 0L;

        return items.stream().mapToLong(item -> item.price() * item.count()).sum();
    }
}
