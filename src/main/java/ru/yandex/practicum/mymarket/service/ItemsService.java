package ru.yandex.practicum.mymarket.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.mymarket.domain.Item;
import ru.yandex.practicum.mymarket.dto.ItemDto;
import ru.yandex.practicum.mymarket.mapper.ItemDtoMapper;
import ru.yandex.practicum.mymarket.repository.ItemRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;

import static ru.yandex.practicum.mymarket.utils.ListUtils.partition;

@Service
public class ItemsService {

    private final CartService cartService;
    private final ItemRepository itemRepository;

    public ItemsService(CartService cartService, ItemRepository itemRepository) {
        this.cartService = cartService;
        this.itemRepository = itemRepository;
    }

    public List<List<ItemDto>> getItems(String search, String sort, Integer pageNumber, Integer pageSize) {
        PageRequest pageable;
        if ("ALPHA".equals(sort))
            pageable = PageRequest.of(pageNumber - 1, pageSize, Sort.Direction.ASC, "title");
        else if ("PRICE".equals(sort))
            pageable = PageRequest.of(pageNumber - 1, pageSize, Sort.Direction.ASC, "price");
        else
            pageable = PageRequest.of(pageNumber - 1, pageSize);


        Page<ru.yandex.practicum.mymarket.domain.Item> page = search != null && !search.isEmpty() ?
                itemRepository.findAllByTitleContainingOrDescriptionContaining(pageable, search, search) : itemRepository.findAll(pageable);


        List<ItemDto> items = page.get()
                .map(ItemDtoMapper::mapp)
                .toList();

        return partition(items, 3);
    }

    public ItemDto updateCountInCart(Long id, String action) {
        List<ItemDto> itemDtoList = cartService.updateCart(id, action);


        return itemDtoList.stream()
                .filter(itemDto -> Objects.equals(id, itemDto.id()))
                .findFirst().orElseThrow();
    }

    public ItemDto find(Long id) {
        Optional<ru.yandex.practicum.mymarket.domain.Item> itemOptional = itemRepository.findById(id);
        if (itemOptional.isEmpty()) {
            throw new NoSuchElementException();
        }

        return ItemDtoMapper.mapp(itemOptional.get());
    }


    public List<ItemDto> getItems() {
        List<Item> items = itemRepository.findByCountGreaterThan(0);
        return items.stream().map(ItemDtoMapper::mapp).toList();
    }
}
