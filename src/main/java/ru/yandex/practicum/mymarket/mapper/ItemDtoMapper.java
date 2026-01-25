package ru.yandex.practicum.mymarket.mapper;

import ru.yandex.practicum.mymarket.domain.CartItem;
import ru.yandex.practicum.mymarket.domain.Item;
import ru.yandex.practicum.mymarket.dto.ItemDto;

public class ItemDtoMapper {

    public static ItemDto mapp(Item item) {
        return mapp(item, 0);
    }

    public static ItemDto mapp(CartItem cartItem) {
        return new ItemDto(cartItem.getItemId(), null, null, null, null, cartItem.getCount());
//        return mapp(null, cartItem.getCount());
//        return mapp(cartItem.getItem(), cartItem.getCount());
    }

    private static ItemDto mapp(Item item, Integer count) {
        return new ItemDto(item.getId(), item.getTitle(), item.getDescription(), item.getImgPath(), item.getPrice(), count);
    }
}
