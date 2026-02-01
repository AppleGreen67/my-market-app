package ru.yandex.practicum.mymarket.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.yandex.practicum.mymarket.dto.ItemDto;
import ru.yandex.practicum.mymarket.repository.ItemDatabaseClientRepository;

@Service
public class ItemsService {

    private final CartService cartService;
    private final ItemDatabaseClientRepository itemRepository;

    public ItemsService(CartService cartService, ItemDatabaseClientRepository itemRepository) {
        this.cartService = cartService;
        this.itemRepository = itemRepository;
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
        return itemRepository.findAll(search);
    }

    public Mono<ItemDto> updateCountInCart(Long itemId, String action, Long userId) {
        return cartService.updateCart(itemId, action, userId)
                .filter(itemDto -> itemId.equals(itemDto.getId())).next();
    }

    @Transactional
    public Mono<ItemDto> find(Long itemId, Long userId) {
        return itemRepository.findById(itemId);
    }
}
