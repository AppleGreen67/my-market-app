package ru.yandex.practicum.mymarket.dto;

public class OrderItemDto implements IItem {
    private Long id;
    private String title;
    private Long price;
    private Integer count;

    public OrderItemDto() {
    }

    public OrderItemDto(Long id, String title, Long price, Integer count) {
        this.id = id;
        this.title = title;
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
