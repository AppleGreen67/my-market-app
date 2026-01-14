package ru.yandex.practicum.mymarket.repository;

import org.springframework.data.repository.CrudRepository;
import ru.yandex.practicum.mymarket.domain.Order;

import java.util.List;

public interface OrderRepository extends CrudRepository<Order, Long> {

    @Override
    List<Order> findAll();
}
