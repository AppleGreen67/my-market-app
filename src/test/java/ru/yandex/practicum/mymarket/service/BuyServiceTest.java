package ru.yandex.practicum.mymarket.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import ru.yandex.practicum.mymarket.dto.ItemDto;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = BuyService.class)
class BuyServiceTest {

    @MockitoBean
    private CartService cartService;
    @MockitoBean
    private OrderService orderService;

    @Autowired
    private BuyService service;

    @Test
    void buy_noCart_exception() {
        Long userId = 1L;

        when(cartService.getCartItems(userId)).thenReturn(Flux.empty());

        StepVerifier.create(service.buy(userId))
                .expectError(NoSuchElementException.class)
                .verify();

        verify(cartService).getCartItems(userId);
        verify(orderService, never()).create(userId);
        verify(orderService, never()).saveItems(anyList(), any());
    }

    @Test
    void buy() {
        Long userId = 17L;

        when(cartService.getCartItems(userId)).thenReturn(Flux.just(new ItemDto(), new ItemDto()));

        Long orderId = 1L;
        when(orderService.create(userId)).thenReturn(Mono.just(orderId));

        when(orderService.saveItems(anyList(), eq(orderId))).thenReturn(Mono.just(2L));

        StepVerifier.create(service.buy(userId))
                .expectNextMatches(buyId -> {
                    assertEquals(orderId, buyId);
                    return true;
                })
                .verifyComplete();

        verify(cartService).getCartItems(userId);
        verify(orderService).create(userId);
        verify(orderService).saveItems(anyList(), eq(orderId));
    }
}