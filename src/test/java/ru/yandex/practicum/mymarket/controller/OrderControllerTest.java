package ru.yandex.practicum.mymarket.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.mymarket.dto.Item;
import ru.yandex.practicum.mymarket.dto.Order;
import ru.yandex.practicum.mymarket.service.OrderService;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.clearInvocations;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @MockitoBean
    private OrderService orderService;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void beforeTest() {
        clearInvocations(orderService);
    }

    @Test
    void getOrders() throws Exception {
        List<Item> items = Arrays.asList(new Item(1L, "title1", "description1", "imageUrl", 11L, 111L),
                new Item(2L, "title2", "description2", "imageUrl", 22L, 222L),
                new Item(3L, "title3", "description3", "imageUrl", 33L, 333L));

        when(orderService.getOrders()).thenReturn(Collections.singletonList(new Order(1L, items, 8090L)));

        mockMvc.perform(get("/orders"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Сумма: 8090 руб.")))
                .andExpect(content().contentType("text/html;charset=UTF-8"));

        verify(orderService).getOrders();
    }

    @Test
    void getOrder() throws Exception {
        long id = 1;

        List<Item> items = Arrays.asList(new Item(1L, "title1", "description1", "imageUrl", 11L, 111L),
                new Item(2L, "title2", "description2", "imageUrl", 22L, 222L),
                new Item(3L, "title3", "description3", "imageUrl", 33L, 333L));

        when(orderService.find(id)).thenReturn(new Order(1L, items, 7890L));

        mockMvc.perform(get("/orders/{id}?newOrder={newOrder}", id, false))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Сумма: 7890 руб.")))
                .andExpect(content().contentType("text/html;charset=UTF-8"));

        verify(orderService).find(id);
    }
}