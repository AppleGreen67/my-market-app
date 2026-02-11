package ru.yandex.practicum.mymarket.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import ru.yandex.practicum.mymarket.domain.Order;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataR2dbcTest
class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderCrudRepository;

    @Test
    public void create() {
        Order order = new Order();
        order.setUserId(17L);
        orderCrudRepository.save(order).block();

        assertNotNull(order.getId());
    }
}