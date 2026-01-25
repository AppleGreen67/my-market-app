package ru.yandex.practicum.mymarket.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import ru.yandex.practicum.mymarket.domain.Cart;
import ru.yandex.practicum.mymarket.domain.CartItem;
import ru.yandex.practicum.mymarket.domain.Item;
import ru.yandex.practicum.mymarket.dto.ItemDto;
import ru.yandex.practicum.mymarket.mapper.ItemDtoMapper;
import ru.yandex.practicum.mymarket.repository.CartRepository;
import ru.yandex.practicum.mymarket.repository.ItemRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final ItemRepository itemRepository;

    public CartService(CartRepository cartRepository, ItemRepository itemRepository) {
        this.cartRepository = cartRepository;
        this.itemRepository = itemRepository;
    }

    public Flux<ItemDto> getCartItems(Long userId) {
        return null;
//        return cartRepository.findByUserId(userId)
//                .flatMapMany(cart -> Flux.fromStream(cart.getItems().stream()
//                        .map(ItemDtoMapper::mapp)));
    }

    @Transactional
    public Flux<ItemDto> updateCart(Long id, String action, Long userId) {
//        Optional<Cart> cartOptional = cartRepository.findByUserId(userId);
//        Cart cart;
//        if (cartOptional.isEmpty()) {
//            cart = new Cart();
//            cart.setUserId(userId);
//            cartRepository.save(cart);
//        } else {
//            cart = cartOptional.get();
//        }
//
//        Optional<CartItem> cartItemOptional = findCartItemByItem(cart, id);
//        if (cartItemOptional.isEmpty()) {
//            Optional<Item> itemOptional = itemRepository.findById(id);
//            if (itemOptional.isEmpty()) throw new NoSuchElementException();
//
//            CartItem cartItem = new CartItem();
//            cartItem.setCart(cart);
//            cartItem.setCount(1);
//            cartItem.setItem(itemOptional.get());
//
//            cart.getItems().add(cartItem);
//        } else {
//            CartItem cartItem = cartItemOptional.get();
//
//            Integer newCount = getCount(cartItem.getCount(), action);
//            if (newCount == 0)
//                cart.getItems().remove(cartItem);
//            else
//                cartItem.setCount(newCount);
//        }
//
//        cartRepository.save(cart);
//
//        return cart.getItems().stream()
//                .map(ItemDtoMapper::mapp).toList();
        return null;
    }

    public Optional<CartItem> findCartItemByItem(Cart cart, Long itemId) {
        return null;
        //        return cart.getItems().stream()
//                .filter(cartItem -> cartItem.getItem().getId().equals(itemId))
//                .findFirst();
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

}
