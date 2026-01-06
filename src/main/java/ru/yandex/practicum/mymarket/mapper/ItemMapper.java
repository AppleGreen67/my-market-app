package ru.yandex.practicum.mymarket.mapper;

import ru.yandex.practicum.mymarket.domain.Item;
import ru.yandex.practicum.mymarket.dto.ItemDto;

public class ItemMapper {

    public static ItemDto mapp(Item item) {
        return new ItemDto(item.getId(), item.getTitle(), item.getDescription(), item.getImgPath(), item.getPrice(), item.getCount());
    }
}
