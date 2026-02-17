package ru.yandex.practicum.mymarket.utils;

public class KeyUtils {

    public static String createKey(String search, String sort, Integer pageNumber, Integer pageSize) {
        return new StringBuilder()
                .append("items:")
                .append(search == null ? "" : search)
                .append(":")
                .append(sort)
                .append(":")
                .append(pageNumber)
                .append(":")
                .append(pageSize)
                .append(":").toString();
    }
}
