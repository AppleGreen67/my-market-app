package ru.yandex.practicum.mymarket.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.yandex.practicum.mymarket.domain.Cart;
import ru.yandex.practicum.mymarket.domain.CartItem;
import ru.yandex.practicum.mymarket.domain.Item;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.yandex.practicum.mymarket.utils.ItemsUtils.getItem;

@DataJpaTest
class CartRepositoryTest {

    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private CartRepository repository;

    @BeforeEach
    void setUp() {
        itemRepository.deleteAll();
        repository.deleteAll();
    }

    @Test
    void findByUserId_noCart() {
        Long userId = 1L;

        Optional<Cart> cartOptional = repository.findByUserId(userId);
        assertTrue(cartOptional.isEmpty());
    }

    @Test
    void findByUserId() {
        Long userId = 1L;

        Item item = getItem("title2", "description2", "imagePath", 22L);
        itemRepository.save(item);

        CartItem cartItem = new CartItem();
        cartItem.setItem(item);
        cartItem.setCount(3);

        Cart cart = new Cart();
        cartItem.setCart(cart);
        cart.getItems().add(cartItem);
        cart.setUserId(userId);

        repository.save(cart);

        Optional<Cart> cartOptional = repository.findByUserId(userId);
        assertTrue(cartOptional.isPresent());
        assertNotNull(cartOptional.get().getId());
        assertEquals(userId, cartOptional.get().getUserId());
        assertEquals(1, cartOptional.get().getItems().size());
    }
}