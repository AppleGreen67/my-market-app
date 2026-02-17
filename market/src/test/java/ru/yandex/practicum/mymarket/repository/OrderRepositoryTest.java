package ru.yandex.practicum.mymarket.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.mymarket.domain.Order;
import ru.yandex.practicum.mymarket.redis.RedisTestCacheConfig;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataR2dbcTest
@Import(RedisTestCacheConfig.class)
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