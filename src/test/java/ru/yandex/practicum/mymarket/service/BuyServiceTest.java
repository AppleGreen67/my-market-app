package ru.yandex.practicum.mymarket.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import ru.yandex.practicum.mymarket.domain.Cart;
import ru.yandex.practicum.mymarket.domain.CartItem;
import ru.yandex.practicum.mymarket.domain.Order;
import ru.yandex.practicum.mymarket.repository.CartRepository;
import ru.yandex.practicum.mymarket.repository.OrderRepository;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static ru.yandex.practicum.mymarket.service.CartService.USER_ID;
import static ru.yandex.practicum.mymarket.utils.ItemsUtils.getItem;

@SpringBootTest(classes = BuyService.class)
class BuyServiceTest {

    @MockitoBean
    private CartRepository cartRepository;
    @MockitoBean
    private OrderRepository orderRepository;

    @Autowired
    private BuyService service;

    @Test
    void buy_noCart_exception() {
        try {
            service.buy();
            fail();
        } catch (Exception e) {
            assertInstanceOf(NoSuchElementException.class, e);
        }

        verify(cartRepository).findByUserId(USER_ID);
        verify(orderRepository, never()).save(any());
        verify(cartRepository, never()).save(any());
    }

    @Test
    void buy() {
        CartItem cartItem = new CartItem();
        cartItem.setId(1L);
        cartItem.setItem(getItem(2L, "title2", "description2", "imagePath", 22L));
        cartItem.setCount(3);

        Cart cart = new Cart();
        cart.getItems().add(cartItem);

        when(cartRepository.findByUserId(USER_ID)).thenReturn(Optional.of(cart));

        doAnswer(in -> {
            Order orderInSave = in.getArgument(0);
            orderInSave.setId(1L);

            assertEquals(1, orderInSave.getOrderItems().size());
            assertEquals(cartItem.getCount(), orderInSave.getOrderItems().getFirst().getCount());

            return orderInSave;
        }).when(orderRepository).save(any(Order.class));

        doAnswer(in -> {
            Cart cartInSave = in.getArgument(0);

            assertEquals(0, cartInSave.getItems().size());

            return cartInSave;
        }).when(cartRepository).save(any(Cart.class));

        Long orderId = service.buy();
        assertEquals(1, orderId);

        verify(cartRepository).findByUserId(USER_ID);
    }
}