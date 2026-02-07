package ru.yandex.practicum.mymarket.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.yandex.practicum.mymarket.dto.OrderDto;
import ru.yandex.practicum.mymarket.dto.OrderItemDto;
import ru.yandex.practicum.mymarket.service.OrderService;
import ru.yandex.practicum.mymarket.service.user.IUserService;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.clearInvocations;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@WebFluxTest(OrderController.class)
class OrderControllerTest {

    @MockitoBean
    private IUserService userService;
    @MockitoBean
    private OrderService orderService;

    @Autowired
    private WebTestClient webTestClient;

    @BeforeEach
    void beforeTest() {
        clearInvocations(orderService);
        clearInvocations(userService);
    }

    @Test
    void getOrders() {
        List<OrderItemDto> items = Arrays.asList(new OrderItemDto(1L, "title1", 11L, 111),
                new OrderItemDto(2L, "title2", 22L, 222),
                new OrderItemDto(3L, "title3", 33L, 333));

        Long userId = 17L;
        when(userService.getCurrentUserId()).thenReturn(Mono.just(userId));

        when(orderService.getOrders(userId))
                .thenReturn(Flux.just(new OrderDto(1L, items, 8090L)));

        webTestClient.get()
                .uri("/orders")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentTypeCompatibleWith("text/html;charset=UTF-8")
                .expectBody(String.class)
                .value(html -> {
                    assert html.contains("Сумма: 8090 руб.");
                });

        verify(orderService).getOrders(any());
    }

    @Test
    void getOrder() {
        Long orderId = 1L;
        Long userId = 17l;

        List<OrderItemDto> items = Arrays.asList(new OrderItemDto(1L, "title1", 11L, 111),
                new OrderItemDto(2L, "title2", 22L, 222),
                new OrderItemDto(3L, "title3", 33L, 333));

        when(userService.getCurrentUserId()).thenReturn(Mono.just(userId));

        when(orderService.find(userId, orderId)).thenReturn(Mono.just(new OrderDto(1L, items, 7890L)));

        webTestClient.get()
                .uri("/orders/{id}?newOrder={newOrder}", orderId, false)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentTypeCompatibleWith("text/html;charset=UTF-8")
                .expectBody(String.class)
                .value(html -> {
                    assert html.contains("Сумма: 7890 руб.");
                });

        verify(orderService).find(userId, orderId);
    }
}