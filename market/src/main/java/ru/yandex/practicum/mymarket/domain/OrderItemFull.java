package ru.yandex.practicum.mymarket.domain;

public class OrderItemFull {
    private Long orderId;
    private Long itemId;
    private String title;
    private Long price;
    private Integer itemCount;

    public Long getOrderId() {
        return orderId;
    }

    public OrderItemFull setOrderId(Long orderId) {
        this.orderId = orderId;
        return this;
    }

    public Long getItemId() {
        return itemId;
    }

    public OrderItemFull setItemId(Long itemId) {
        this.itemId = itemId;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public OrderItemFull setTitle(String title) {
        this.title = title;
        return this;
    }

    public Long getPrice() {
        return price;
    }

    public OrderItemFull setPrice(Long price) {
        this.price = price;
        return this;
    }

    public Integer getItemCount() {
        return itemCount;
    }

    public OrderItemFull setItemCount(Integer itemCount) {
        this.itemCount = itemCount;
        return this;
    }
}
