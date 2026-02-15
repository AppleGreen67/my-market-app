package ru.yandex.practicum.mymarket.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.yandex.practicum.mymarket.model.UserBalance;

@Service
public class PayService {

    private WebClient webClient = WebClient.create("http://pay:8080");

    public Mono<Boolean> checkBalance(Long userId, Mono<Long> sum) {
        Mono<UserBalance> responseBody = webClient.get()
                .uri("/{userId}/balance", userId)
                .retrieve()
                .bodyToMono(UserBalance.class);
        return responseBody
                .flatMap(userBalance -> {
                    System.out.println(userBalance);
                    return  sum.map(s ->  userBalance.getBalance() - s >= 0);
                });
    }
}
