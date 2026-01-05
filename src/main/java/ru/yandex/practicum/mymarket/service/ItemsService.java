package ru.yandex.practicum.mymarket.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.mymarket.dto.Item;
import ru.yandex.practicum.mymarket.repository.ItemRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class ItemsService {

    private final ItemRepository itemRepository;

    public ItemsService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public List<List<Item>> getItems(String search, String sort, Integer pageNumber, Integer pageSize) {
        PageRequest pageable;
        if ("ALPHA".equals(sort))
            pageable = PageRequest.of(pageNumber - 1, pageSize, Sort.Direction.ASC, "title");
        else if ("PRICE".equals(sort))
            pageable = PageRequest.of(pageNumber - 1, pageSize, Sort.Direction.ASC, "price");
        else
            pageable = PageRequest.of(pageNumber - 1, pageSize);


        Page<ru.yandex.practicum.mymarket.domain.Item> page = search != null && !search.isEmpty() ?
                itemRepository.findAllByTitleContainingOrDescriptionContaining(pageable, search, search) : itemRepository.findAll(pageable);

        List<Item> items = page.get()
                .map(item -> new Item(item.getId(), item.getTitle(), item.getDescription(), item.getImgPath(), item.getPrice(), 0L))
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


    public Item updateCount(Long id, String action) {
        //todo
        return new Item(2L, "title1", "description1", "imageUrl", 0L, 0L);
    }

    public Item find(Long id) {
        Optional<ru.yandex.practicum.mymarket.domain.Item> itemOptional = itemRepository.findById(id);
        if (itemOptional.isEmpty()) {
            throw new NoSuchElementException();
        }

        ru.yandex.practicum.mymarket.domain.Item item = itemOptional.get();
        return new Item(item.getId(), item.getTitle(), item.getDescription(), item.getImgPath(), item.getPrice(), 0L);
    }

}
