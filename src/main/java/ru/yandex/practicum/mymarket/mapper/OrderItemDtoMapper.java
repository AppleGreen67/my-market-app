package ru.yandex.practicum.mymarket.mapper;

import ru.yandex.practicum.mymarket.domain.Item;
import ru.yandex.practicum.mymarket.domain.OrderItem;
import ru.yandex.practicum.mymarket.dto.OrderItemDto;

public class OrderItemDtoMapper {

    public static OrderItemDto mapp(OrderItem orderItem) {
        Item item = orderItem.getItem();
        return new OrderItemDto(item.getId(), item.getTitle(), item.getPrice(), orderItem.getCount());
    }
}
