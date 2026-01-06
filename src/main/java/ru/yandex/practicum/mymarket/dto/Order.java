package ru.yandex.practicum.mymarket.dto;

import java.util.List;

public class Order {
    private Long id;
    private List<ItemDto> items;
    private Long totalSum;

    public Order() {
    }

    public Order(Long id, List<ItemDto> items, Long totalSum) {
        this.id = id;
        this.items = items;
        this.totalSum = totalSum;
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

    public List<ItemDto> getItems() {
        return items;
    }

    public List<ItemDto> items() {
        return items;
    }

    public void setItems(List<ItemDto> items) {
        this.items = items;
    }

    public Long getTotalSum() {
        return totalSum;
    }

    public Long totalSum() {
        return totalSum;
    }

    public void setTotalSum(Long totalSum) {
        this.totalSum = totalSum;
    }
}
