package ru.yandex.practicum.mymarket.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
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

    public static final long USER_ID = 1L;

    private final CartRepository cartRepository;
    private final ItemRepository itemRepository;

    public CartService(CartRepository cartRepository, ItemRepository itemRepository) {
        this.cartRepository = cartRepository;
        this.itemRepository = itemRepository;
    }

    public List<ItemDto> getCartItems() {
        Optional<Cart> cartOptional = cartRepository.findByUserId(USER_ID);

        return cartOptional.map(cart -> cart.getItems().stream()
                .map(ItemDtoMapper::mapp).toList()).orElseGet(ArrayList::new);

    }

    @Transactional
    public List<ItemDto> updateCart(Long id, String action) {
        Optional<Cart> cartOptional = cartRepository.findByUserId(USER_ID);
        Cart cart;
        if (cartOptional.isEmpty()) {
            cart = new Cart();
            cart.setUserId(USER_ID);
            cartRepository.save(cart);
        } else {
            cart = cartOptional.get();
        }

        Optional<CartItem> cartItemOptional = findCartItemByItem(cart, id);
        if (cartItemOptional.isEmpty()) {
            Optional<Item> itemOptional = itemRepository.findById(id);
            if (itemOptional.isEmpty()) throw new NoSuchElementException();

            CartItem cartItem = new CartItem();
            cartItem.setCart(cart);
            cartItem.setCount(1);
            cartItem.setItem(itemOptional.get());

            cart.getItems().add(cartItem);
        } else {
            CartItem cartItem = cartItemOptional.get();

            Integer newCount = getCount(cartItem.getCount(), action);
            if (newCount == 0)
                cart.getItems().remove(cartItem);
            else
                cartItem.setCount(newCount);
        }

        cartRepository.save(cart);

        return cart.getItems().stream()
                .map(ItemDtoMapper::mapp).toList();
    }

    public Optional<CartItem> findCartItemByItem(Cart cart, Long itemId) {
         return cart.getItems().stream()
                .filter(cartItem -> cartItem.getItem().getId().equals(itemId))
                .findFirst();
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
