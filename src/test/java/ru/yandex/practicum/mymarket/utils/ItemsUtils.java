package ru.yandex.practicum.mymarket.utils;

import ru.yandex.practicum.mymarket.domain.Item;

public class ItemsUtils {
    public static Item getItem(long id, String title2, String description2, String imagePath, long price) {
        Item item = new Item();
        item.setId(id);
        item.setTitle(title2);
        item.setDescription(description2);
        item.setImgPath(imagePath);
        item.setPrice(price);
        return item;
    }
}
