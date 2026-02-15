package ru.yandex.practicum.mymarket.service;

import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.yandex.practicum.mymarket.model.PayRequest;
import ru.yandex.practicum.mymarket.model.UserBalance;

@Service
public class PayService {

    private WebClient webClient = WebClient.create("http://pay:8080");

    public Mono<Boolean> checkBalance(Long userId, Mono<Long> sum) {
        return webClient.get()
                .uri("/{userId}/balance", userId)
                .retrieve()
                .bodyToMono(UserBalance.class)
                .flatMap(userBalance -> sum.map(s -> userBalance.getBalance() - s >= 0));
    }

    public Mono<Boolean> pay(Long userId, Long sum) {
        return webClient.post()
                .uri("/pay")
                .bodyValue(createPayRequest(userId, sum))
                .retrieve()
//                .onStatus(HttpStatusCode::is4xxClientError, clientResponse ->
//                        Mono.error(new RuntimeException("Client error: " + clientResponse.statusCode()))
//                )
                .bodyToMono(UserBalance.class)
                .map(userBalance -> userBalance.getBalance() >= 0);
    }

    private PayRequest createPayRequest(Long userId, Long sum) {
        PayRequest payRequest = new PayRequest();
        payRequest.setUserId(userId);
        payRequest.setAmount(sum);
        return payRequest;
    }
}
