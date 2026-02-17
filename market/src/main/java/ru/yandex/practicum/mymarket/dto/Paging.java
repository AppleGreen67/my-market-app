package ru.yandex.practicum.mymarket.dto;

public class Paging {
    private Integer pageNumber;
    private Integer pageSize;
    private boolean hasPrevious;
    private boolean hasNext;

    public Paging() {
    }

    public Paging(Integer pageNumber, Integer pageSize) {
        this.pageSize = pageSize;
        this.pageNumber = pageNumber;
    }

    public Integer pageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer pageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public boolean hasPrevious() {
        return hasPrevious;
    }

    public void setHasPrevious(boolean hasPrevious) {
        this.hasPrevious = hasPrevious;
    }

    public boolean hasNext() {
        return hasNext;
    }

    public void setHasNext(boolean hasNext) {
        this.hasNext = hasNext;
    }
}
