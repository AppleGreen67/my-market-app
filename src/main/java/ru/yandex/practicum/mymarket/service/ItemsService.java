package ru.yandex.practicum.mymarket.service;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.mymarket.domain.Cart;
import ru.yandex.practicum.mymarket.domain.CartItem;
import ru.yandex.practicum.mymarket.dto.ItemDto;
import ru.yandex.practicum.mymarket.mapper.ItemDtoMapper;
import ru.yandex.practicum.mymarket.repository.CartRepository;
import ru.yandex.practicum.mymarket.repository.ItemRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;

import static ru.yandex.practicum.mymarket.service.CartService.USER_ID;
import static ru.yandex.practicum.mymarket.utils.ListUtils.partition;

@Service
public class ItemsService {

    private final CartService cartService;
    private final ItemRepository itemRepository;
    private final CartRepository cartRepository;

    public ItemsService(CartService cartService, ItemRepository itemRepository, CartRepository cartRepository) {
        this.cartService = cartService;
        this.itemRepository = itemRepository;
        this.cartRepository = cartRepository;
    }

    @Transactional
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

        Optional<Cart> cartOptional = cartRepository.findByUserId(USER_ID);
        List<ItemDto> items;
        if (cartOptional.isPresent()) {
            Cart cart = cartOptional.get();
            items = page.get()
                    .map(item -> {
                        Optional<CartItem> cartItemOptional = cartService.findCartItemByItem(cart, item.getId());
                        return cartItemOptional.map(ItemDtoMapper::mapp).orElseGet(() -> ItemDtoMapper.mapp(item));
                    }).toList();

        } else {
            items = page.get()
                    .map(ItemDtoMapper::mapp)
                    .toList();
        }

        return partition(items, 3);
    }

    public ItemDto updateCountInCart(Long id, String action) {
        List<ItemDto> itemDtoList = cartService.updateCart(id, action);

        return itemDtoList.stream()
                .filter(itemDto -> Objects.equals(id, itemDto.id()))
                .findFirst().orElseThrow();
    }

    @Transactional
    public ItemDto find(Long id) {
        Optional<ru.yandex.practicum.mymarket.domain.Item> itemOptional = itemRepository.findById(id);
        if (itemOptional.isEmpty()) throw new NoSuchElementException();

        Optional<Cart> cartOptional = cartRepository.findByUserId(USER_ID);
        if (cartOptional.isPresent()) {
            Optional<CartItem> cartItemOptional = cartService.findCartItemByItem(cartOptional.get(), id);
            if (cartItemOptional.isPresent())
                return ItemDtoMapper.mapp(cartItemOptional.get());
        }

        return ItemDtoMapper.mapp(itemOptional.get());
    }
}
