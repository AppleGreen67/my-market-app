package ru.yandex.practicum.mymarket.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.MockMvc;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.yandex.practicum.mymarket.dto.ItemDto;
import ru.yandex.practicum.mymarket.dto.OrderDto;
import ru.yandex.practicum.mymarket.dto.OrderItemDto;
import ru.yandex.practicum.mymarket.service.OrderService;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.clearInvocations;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebFluxTest(OrderController.class)
class OrderControllerTest {

    @MockitoBean
    private OrderService orderService;

    @Autowired
    private WebTestClient webTestClient;

    @BeforeEach
    void beforeTest() {
        clearInvocations(orderService);
    }

    @Test
    void getOrders() {
        List<OrderItemDto> items = Arrays.asList(new OrderItemDto(1L, "title1", 11L, 111),
                new OrderItemDto(2L, "title2", 22L, 222),
                new OrderItemDto(3L, "title3", 33L, 333));

        when(orderService.getOrders())
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

        verify(orderService).getOrders();
    }

    @Test
    void getOrder() {
        long id = 1;

        List<OrderItemDto> items = Arrays.asList(new OrderItemDto(1L, "title1", 11L, 111),
                new OrderItemDto(2L, "title2", 22L, 222),
                new OrderItemDto(3L, "title3", 33L, 333));

        when(orderService.find(id)).thenReturn(Mono.just(new OrderDto(1L, items, 7890L)));

        webTestClient.get()
                .uri("/orders/{id}?newOrder={newOrder}", id, false)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentTypeCompatibleWith("text/html;charset=UTF-8")
                .expectBody(String.class)
                .value(html -> {
                    assert html.contains("Сумма: 7890 руб.");
                });

        verify(orderService).find(id);
    }
}