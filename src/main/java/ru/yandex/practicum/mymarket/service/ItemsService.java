package ru.yandex.practicum.mymarket.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.yandex.practicum.mymarket.domain.Cart;
import ru.yandex.practicum.mymarket.domain.CartItem;
import ru.yandex.practicum.mymarket.domain.Item;
import ru.yandex.practicum.mymarket.dto.ItemDto;
import ru.yandex.practicum.mymarket.mapper.ItemDtoMapper;
import ru.yandex.practicum.mymarket.repository.CartItemRepository;
import ru.yandex.practicum.mymarket.repository.CartRepository;
import ru.yandex.practicum.mymarket.repository.ItemDatabaseClientRepository;
import ru.yandex.practicum.mymarket.repository.ItemRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;

import static ru.yandex.practicum.mymarket.utils.ListUtils.partition;

@Service
public class ItemsService {

    private final CartService cartService;
    private final ItemDatabaseClientRepository itemRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    public ItemsService(CartService cartService, ItemDatabaseClientRepository itemRepository, CartRepository cartRepository, CartItemRepository cartItemRepository) {
        this.cartService = cartService;
        this.itemRepository = itemRepository;
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
    }

    @Transactional
    public Flux<ItemDto> getItems(Long userId, String search, String sort, Integer pageNumber, Integer pageSize) {
        PageRequest pageable;
        if ("ALPHA".equals(sort))
            pageable = PageRequest.of(pageNumber - 1, pageSize, Sort.Direction.ASC, "title");
        else if ("PRICE".equals(sort))
            pageable = PageRequest.of(pageNumber - 1, pageSize, Sort.Direction.ASC, "price");
        else
            pageable = PageRequest.of(pageNumber - 1, pageSize);


//        Page<Item> page = search != null && !search.isEmpty() ?
//                itemRepository.findAllByTitleContainingOrDescriptionContaining(pageable, search, search) : itemRepository.findAll(pageable);
//
//        Optional<Cart> cartOptional = cartRepository.findByUserId(userId);
//        List<ItemDto> items;
//        if (cartOptional.isPresent()) {
//            Cart cart = cartOptional.get();
//            items = page.get()
//                    .map(item -> {
//                        Optional<CartItem> cartItemOptional = cartService.findCartItemByItem(cart, item.getId());
//                        return cartItemOptional.map(ItemDtoMapper::mapp).orElseGet(() -> ItemDtoMapper.mapp(item));
//                    }).toList();
//
//        } else {
//            items = page.get()
//                    .map(ItemDtoMapper::mapp)
//                    .toList();
//        }
//
//        return partition(items, 3);
        return null;
    }

    public Mono<ItemDto> updateCountInCart(Long id, String action, Long userId) {
//        List<ItemDto> itemDtoList = cartService.updateCart(id, action, userId);
//
//        return itemDtoList.stream()
//                .filter(itemDto -> Objects.equals(id, itemDto.getId()))
//                .findFirst().orElseThrow();
        return null;
    }

    @Transactional
    public Mono<ItemDto>  find(Long id, Long userId) {
        return itemRepository.findById(id);
    }
}
