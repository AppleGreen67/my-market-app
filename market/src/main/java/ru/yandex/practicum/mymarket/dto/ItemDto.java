package ru.yandex.practicum.mymarket.dto;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash(value = "item")
public class ItemDto implements IItem{
    @Id
    private Long id;
    private String title;
    private String description;
    private String imgPath;
    private Long price;
    private Integer count;

    public ItemDto() {
    }

    public ItemDto(Long id, String title, String description, String imgPath, Long price, Integer count) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.imgPath = imgPath;
        this.price = price;
        this.count = count;
    }

    public Long getId() {
        return id;
    }

    public Long id() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public String title() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public String description() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImgPath() {
        return imgPath;
    }

    public String imgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public Long getPrice() {
        return price;
    }

    public Long price() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public Integer getCount() {
        return count;
    }

    public Integer count() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
