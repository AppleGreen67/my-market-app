package ru.yandex.practicum.mymarket.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import ru.yandex.practicum.mymarket.dto.IItem;
import ru.yandex.practicum.mymarket.dto.ItemDto;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = SumService.class)
class SumServiceTest {

    @Autowired
    private SumService service;

    @Test
    void calculateSum() {
        Flux<IItem> items = Flux.just(new ItemDto(2L, "title2", "description2", "imageUrl", 2L, 3),
                new ItemDto(3L, "title3", "description3", "imageUrl", 11L, 3));

        StepVerifier.create(service.calculateSum(items))
                .expectNext(39L)
                .verifyComplete();
    }
}