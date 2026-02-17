package ru.yandex.practicum.pay;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import ru.yandex.practicum.pay.model.PayRequest;
import ru.yandex.practicum.pay.model.UserBalance;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.clearInvocations;
import static org.mockito.Mockito.when;

@WebFluxTest(PayController.class)
class PayControllerTest {

    @MockitoBean
    private BalanceService balanceService;

    @Autowired
    private WebTestClient webTestClient;

    @BeforeEach
    void beforeTest() {
        clearInvocations(balanceService);
    }

    @Test
    void payPost() {
        Long userId = 17L;
        Long balance = 305L;

        when(balanceService.getBalance(userId)).thenReturn(balance);

        PayRequest payRequest = new PayRequest();
        payRequest.setUserId(userId);
        payRequest.setAmount(105L);


        webTestClient.post()
                .uri("/pay")
                .bodyValue(payRequest)
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserBalance.class)
                .value(userBalance -> {
                    assertEquals(userId, userBalance.getUserId());
                    assertEquals(200, userBalance.getBalance());
                });
    }

    @Test
    void payPost_minus() {
        Long userId = 17L;
        Long balance = 305L;

        when(balanceService.getBalance(userId)).thenReturn(balance);

        PayRequest payRequest = new PayRequest();
        payRequest.setUserId(userId);
        payRequest.setAmount(405L);


        webTestClient.post()
                .uri("/pay")
                .bodyValue(payRequest)
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserBalance.class)
                .value(userBalance -> {
                    assertEquals(userId, userBalance.getUserId());
                    assertEquals(-100, userBalance.getBalance());
                });
    }
}