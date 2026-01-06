package ru.yandex.practicum.mymarket.dto;

public class ItemDto {
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

    public Long id() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String title() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String description() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String imgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public Long price() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public Integer count() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
