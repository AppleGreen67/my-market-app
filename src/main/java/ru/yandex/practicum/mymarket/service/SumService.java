package ru.yandex.practicum.mymarket.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.mymarket.dto.IItem;

import java.util.List;

@Service
public class SumService {
    public Long calculateSum(List<? extends IItem> items) {
        if (items == null || items.isEmpty()) return 0L;

        return items.stream().mapToLong(item -> item.getPrice() * item.getCount()).sum();
    }
}
