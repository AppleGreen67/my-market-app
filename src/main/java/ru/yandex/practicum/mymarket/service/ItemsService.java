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
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class ItemsService {

    protected final ItemRepository itemRepository;

    public ItemsService(ItemRepository itemRepository) {
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

    public static <T> List<List<T>> partition(List<T> list, int batchSize) {
        return IntStream.range(0, (list.size() + batchSize - 1) / batchSize)
                .mapToObj(i -> list.subList(
                        i * batchSize,
                        Math.min(list.size(), (i + 1) * batchSize)
                ))
                .collect(Collectors.toList());
    }

    public ItemDto updateCount(Long id, String action) {
        Optional<Item> itemOptional = itemRepository.findById(id);
        if (itemOptional.isEmpty()) throw new NoSuchElementException();


        Item item = itemOptional.get();
        item.setCount(getCount(item.getCount(), action));
        itemRepository.save(item);

        return ItemDtoMapper.mapp(item);
    }

    private Integer getCount(Integer count, String action) {
        if ("MINUS".equals(action))
            return count - 1;
        else if ("PLUS".equals(action))
            return count + 1;
        else if ("DELETE".equals(action))
            return 0;

        throw new UnsupportedOperationException();
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
