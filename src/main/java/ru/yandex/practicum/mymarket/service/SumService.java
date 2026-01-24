package ru.yandex.practicum.mymarket.service;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.yandex.practicum.mymarket.dto.IItem;

@Service
public class SumService {
    public Mono<Long> calculateSum(Flux<? extends IItem> items) {
        return items.map(item -> item.getPrice() * item.getCount())
                .reduce(0L, Long::sum);
    }
}
