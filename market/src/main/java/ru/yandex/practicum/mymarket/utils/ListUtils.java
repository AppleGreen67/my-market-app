package ru.yandex.practicum.mymarket.utils;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ListUtils {
    public static <T> List<List<T>> partition(List<T> list, int batchSize) {
        return IntStream.range(0, (list.size() + batchSize - 1) / batchSize)
                .mapToObj(i -> list.subList(
                        i * batchSize,
                        Math.min(list.size(), (i + 1) * batchSize)
                ))
                .collect(Collectors.toList());
    }
}
