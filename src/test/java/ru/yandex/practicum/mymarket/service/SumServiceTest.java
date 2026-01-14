package ru.yandex.practicum.mymarket.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.mymarket.dto.IItem;
import ru.yandex.practicum.mymarket.dto.ItemDto;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = SumService.class)
class SumServiceTest {

    @Autowired
    private SumService service;

    @Test
    void calculateSum() {
        List<IItem> items = Arrays.asList(new ItemDto(2L, "title2", "description2", "imageUrl", 2L, 3),
                new ItemDto(3L, "title3", "description3", "imageUrl", 11L, 3));
        Long sum = service.calculateSum(items);
        assertEquals(39, sum);
    }
}