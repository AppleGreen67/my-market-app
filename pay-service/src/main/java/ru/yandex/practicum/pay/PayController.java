package ru.yandex.practicum.pay;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/pay")
public class PayController {
    @GetMapping
    public Mono<String> buy() {
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!");
        return Mono.just("Hello from pay");
    }
}
