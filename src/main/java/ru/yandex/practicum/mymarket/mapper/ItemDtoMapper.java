package ru.yandex.practicum.mymarket.mapper;

import ru.yandex.practicum.mymarket.domain.CartItem;
import ru.yandex.practicum.mymarket.domain.Item;
import ru.yandex.practicum.mymarket.dto.ItemDto;

public class ItemDtoMapper {

    public static ItemDto mapp(Item item) {
        return new ItemDto(item.getId(), item.getTitle(), item.getDescription(), item.getImgPath(), item.getPrice(), item.getCount());
    }

    public static ItemDto mapp(CartItem cartItem) {
        Item item = cartItem.getItem();
        return new ItemDto(item.getId(), item.getTitle(), item.getDescription(), item.getImgPath(), item.getPrice(), cartItem.getCount());
    }
}
