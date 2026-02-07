package ru.yandex.practicum.mymarket.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.yandex.practicum.mymarket.dto.ItemDto;
import ru.yandex.practicum.mymarket.service.CartService;
import ru.yandex.practicum.mymarket.service.SumService;
import ru.yandex.practicum.mymarket.service.user.IUserService;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.clearInvocations;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@WebFluxTest(CartController.class)
class CartControllerTest {

    @MockitoBean
    private CartService cartService;
    @MockitoBean
    private IUserService userService;
    @MockitoBean
    private SumService sumService;

    @Autowired
    private WebTestClient webTestClient;

    @BeforeEach
    void beforeTest() {
        clearInvocations(cartService);
        clearInvocations(userService);
    }

    @Test
    void getCartItems(){
         Flux<ItemDto> items = Flux.just(new ItemDto(1L, "title1", "description1", "imageUrl", 11L, 111),
                new ItemDto(2L, "title2", "description2", "imageUrl", 22L, 222),
                new ItemDto(3L, "title3", "description3", "imageUrl", 33L, 333));

        Long userId = 1L;
        when(userService.getCurrentUserId()).thenReturn(Mono.just(userId));

        when(cartService.getCartItems(userId)).thenReturn(items);

        when(sumService.calculateSum(items)).thenReturn(Mono.just(8907L));

        webTestClient.get()
                .uri("/cart/items")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentTypeCompatibleWith("text/html;charset=UTF-8")
                .expectBody(String.class)
                .value(html -> {
                    assert html.contains("<h5 class=\"card-title\">title1</h5>");
                    assert html.contains("<h5 class=\"card-title\">title2</h5>");
                    assert html.contains("<h5 class=\"card-title\">title3</h5>");
                    assert html.contains("Итого: 8907 руб.");
                });

        verify(userService).getCurrentUserId();
        verify(cartService).getCartItems(userId);
        verify(sumService).calculateSum(items);
    }

    @Test
    void changeCount_plus() {
        long id = 1L;
        String action = "PLUS";

         Flux<ItemDto> items = Flux.just(new ItemDto(1L, "title1", "description1", "imageUrl", 11L, 111),
                new ItemDto(2L, "title2", "description2", "imageUrl", 22L, 222),
                new ItemDto(3L, "title3", "description3", "imageUrl", 33L, 333));

        Long userId = 1L;
        when(userService.getCurrentUserId()).thenReturn(Mono.just(userId));

        when(cartService.updateCart(id, action, userId)).thenReturn(items);

        when(sumService.calculateSum(any(Flux.class))).thenReturn(Mono.just(7777L));

        webTestClient.post()
                .uri("/cart/items?id={id}&action={action}", id, action)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentTypeCompatibleWith("text/html;charset=UTF-8")
                .expectBody(String.class)
                .value(html -> {
                    assert html.contains("<h5 class=\"card-title\">title1</h5>");
                    assert html.contains("<h5 class=\"card-title\">title2</h5>");
                    assert html.contains("<h5 class=\"card-title\">title3</h5>");
                    assert html.contains("Итого: 7777 руб.");
                });

        verify(userService).getCurrentUserId();
        verify(cartService).updateCart(id, action, userId);
        verify(sumService).calculateSum(any(Flux.class));
    }

    @Test
    void changeCount_minus() {
        long id = 1L;
        String action = "MINUS";

         Flux<ItemDto> items = Flux.just(new ItemDto(1L, "title1", "description1", "imageUrl", 11L, 111),
                new ItemDto(2L, "title2", "description2", "imageUrl", 22L, 222),
                new ItemDto(3L, "title3", "description3", "imageUrl", 33L, 333));

        Long userId = 1L;
        when(userService.getCurrentUserId()).thenReturn(Mono.just(userId));

        when(cartService.updateCart(id, action, userId)).thenReturn(items);

        when(sumService.calculateSum(any(Flux.class))).thenReturn(Mono.just(666L));

        webTestClient.post()
                .uri("/cart/items?id={id}&action={action}", id, action)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentTypeCompatibleWith("text/html;charset=UTF-8")
                .expectBody(String.class)
                .value(html -> {
                    assert html.contains("<h5 class=\"card-title\">title1</h5>");
                    assert html.contains("<h5 class=\"card-title\">title2</h5>");
                    assert html.contains("<h5 class=\"card-title\">title3</h5>");
                    assert html.contains("Итого: 666 руб.");
                });

        verify(userService).getCurrentUserId();
        verify(cartService).updateCart(id, action, userId);
        verify(sumService).calculateSum(any(Flux.class));
    }

    @Test
    void changeCount_delete(){
        long id = 1L;
        String action = "DELETE";

         Flux<ItemDto> items = Flux.just(new ItemDto(1L, "title1", "description1", "imageUrl", 11L, 111),
                new ItemDto(3L, "title3", "description3", "imageUrl", 33L, 333));

        Long userId = 1L;
        when(userService.getCurrentUserId()).thenReturn(Mono.just(userId));

        when(cartService.updateCart(id, action, userId)).thenReturn(items);

        when(sumService.calculateSum(any(Flux.class))).thenReturn(Mono.just(25L));

        webTestClient.post()
                .uri("/cart/items?id={id}&action={action}", id, action)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentTypeCompatibleWith("text/html;charset=UTF-8")
                .expectBody(String.class)
                .value(html -> {
                    assert html.contains("<h5 class=\"card-title\">title1</h5>");
                    assert html.contains("<h5 class=\"card-title\">title3</h5>");
                    assert html.contains("Итого: 25 руб.");
                });

        verify(userService).getCurrentUserId();
        verify(cartService).updateCart(id, action, userId);
        verify(sumService).calculateSum(any(Flux.class));
    }

    @Test
    void changeCount_exception() {
        long id = 1L;
        String action = "unknown";
        try {
            webTestClient.post()
                    .uri("/cart/items?id={id}&action={action}", id, action)
                    .exchange()
                    .expectStatus().isOk()
                    .expectHeader().contentTypeCompatibleWith("text/html;charset=UTF-8")
                    .expectBody(String.class)
                    .value(html -> {
                        assert html.contains("Error");
                        assert html.contains("Сервис временно недоступен");
                    });
        } catch (Exception e) {
            fail();
            assertInstanceOf(UnsupportedOperationException.class, e.getCause());
        }

        verify(userService, never()).getCurrentUserId();
        verify(cartService, never()).updateCart(eq(id), eq(action), any());
        verify(sumService, never()).calculateSum(any(Flux.class));
    }
}