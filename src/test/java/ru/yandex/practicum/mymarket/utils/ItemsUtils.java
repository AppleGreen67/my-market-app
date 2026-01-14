package ru.yandex.practicum.mymarket.utils;

import ru.yandex.practicum.mymarket.domain.Item;

public class ItemsUtils {
    public static Item getItem(long id, String title, String description, String imagePath, long price) {
        Item item = new Item();
        item.setId(id);
        item.setTitle(title);
        item.setDescription(description);
        item.setImgPath(imagePath);
        item.setPrice(price);
        return item;
    }

    public static Item getItem(String title, String description, String imagePath, long price) {
        Item item = new Item();
        item.setTitle(title);
        item.setDescription(description);
        item.setImgPath(imagePath);
        item.setPrice(price);
        return item;
    }
}
