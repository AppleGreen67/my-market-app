package ru.yandex.practicum.mymarket.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import ru.yandex.practicum.mymarket.domain.Order;
import ru.yandex.practicum.mymarket.domain.OrderItem;
import ru.yandex.practicum.mymarket.dto.OrderDto;
import ru.yandex.practicum.mymarket.repository.OrderRepository;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.clearInvocations;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static ru.yandex.practicum.mymarket.utils.ItemsUtils.getItem;

@SpringBootTest(classes = OrderService.class)
class OrderServiceTest {

    @MockitoBean
    private SumService sumService;
    @MockitoBean
    private OrderRepository orderRepository;

    @Autowired
    private OrderService service;

    @BeforeEach
    void setUp() {
        clearInvocations(orderRepository);
    }

    @Test
    void getOrders() {
        OrderItem orderItem = new OrderItem();
        orderItem.setId(1L);
        orderItem.setCount(2);
        orderItem.setItem(getItem(3L, "title3", "description3", "imagePath", 11L));

        Order order = new Order();
        order.setId(2L);

        orderItem.setOrder(order);
        order.getOrderItems().add(orderItem);

        when(sumService.calculateSum(any())).thenReturn(22L);

        when(orderRepository.findAll()).thenReturn(Collections.singletonList(order));

        List<OrderDto> orders = service.getOrders();
        assertEquals(1, orders.size());
        assertEquals(2, orders.getFirst().getId());
        assertEquals(22, orders.getFirst().getTotalSum());
        assertEquals(1, orders.getFirst().getItems().size());
        assertEquals(3, orders.getFirst().getItems().getFirst().getId());
        assertEquals(2, orders.getFirst().getItems().getFirst().getCount());

        verify(orderRepository).findAll();
    }

    @Test
    void find_noOrder_exception() {
        Long id = 2L;

        when(orderRepository.findById(id)).thenReturn(Optional.empty());

        try {
            service.find(id);
            fail();
        } catch (Exception e) {
            assertInstanceOf(NoSuchElementException.class, e);
        }

        verify(orderRepository).findById(id);
    }

    @Test
    void find() {
        Long id = 2L;

        OrderItem orderItem = new OrderItem();
        orderItem.setId(1L);
        orderItem.setCount(2);
        orderItem.setItem(getItem(3L, "title3", "description3", "imagePath", 11L));

        Order order = new Order();
        order.setId(2L);

        orderItem.setOrder(order);
        order.getOrderItems().add(orderItem);

        when(sumService.calculateSum(any())).thenReturn(22L);

        when(orderRepository.findById(id)).thenReturn(Optional.of(order));

        OrderDto orderDto = service.find(id);
        assertEquals(id, orderDto.getId());
        assertEquals(22, orderDto.getTotalSum());
        assertEquals(1, orderDto.getItems().size());
        assertEquals(3, orderDto.getItems().getFirst().getId());
        assertEquals(2, orderDto.getItems().getFirst().getCount());

        verify(orderRepository).findById(id);
    }
}