package ru.yandex.practicum.mymarket.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.yandex.practicum.mymarket.domain.Item;
import ru.yandex.practicum.mymarket.domain.Order;
import ru.yandex.practicum.mymarket.domain.OrderItem;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static ru.yandex.practicum.mymarket.utils.ItemsUtils.getItem;

@DataJpaTest
class OrderRepositoryTest {

    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private OrderRepository repository;

    @BeforeEach
    void setUp() {
        itemRepository.deleteAll();
        repository.deleteAll();
    }

    @Test
    void findAll() {
        Item item = getItem("title2", "description2", "imagePath", 22L);
        itemRepository.save(item);

        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        orderItem.setCount(2);

        Order order = new Order();
        orderItem.setOrder(order);
        order.getOrderItems().add(orderItem);

        repository.save(order);

        List<Order> orders = repository.findAll();
        assertEquals(1, orders.size());
        assertNotNull(orders.getFirst().getId());
        assertEquals(1, orders.getFirst().getOrderItems().size());
        assertNotNull(orders.getFirst().getOrderItems().getFirst().getId());
        assertEquals(2, orders.getFirst().getOrderItems().getFirst().getCount());
    }
}