package ru.yandex.practicum.mymarket.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import ru.yandex.practicum.mymarket.service.BuyService;
import ru.yandex.practicum.mymarket.service.user.IUserService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.clearInvocations;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@WebFluxTest(BuyController.class)
class BuyControllerTest {

    @MockitoBean
    private BuyService buyService;
    @MockitoBean
    private IUserService userService;

    @Autowired
    private WebTestClient webTestClient;

    @BeforeEach
    void beforeTest() {
        clearInvocations(buyService);
        clearInvocations(userService);
    }

    @Test
    void buy() throws Exception {
        Long userId = 1L;
        when(userService.getCurrentUserId()).thenReturn(Mono.just(userId));

        when(buyService.buy(any())).thenReturn(Mono.just(12L));

        webTestClient.post()
                .uri("/buy")
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().location("/orders/12?newOrder=true");

        verify(userService).getCurrentUserId();
        verify(buyService).buy(userId);
    }
}