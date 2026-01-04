package ru.yandex.practicum.mymarket.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.yandex.practicum.mymarket.dto.Item;
import ru.yandex.practicum.mymarket.dto.Order;

import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/orders")
public class OrderController {

    @GetMapping
    public String getOrders(Model model) {
        List<Item> items = Arrays.asList(new Item(1L, "title", "description", "imageUrl", 0L, 0L),
                new Item(2L, "title1", "description1", "imageUrl", 0L, 0L),
                new Item(2L, "title2", "description2", "imageUrl", 0L, 0L));
        model.addAttribute("orders", Arrays.asList(new Order(1L, items, 8907L)));

        return "orders";
    }

    @GetMapping("/{id}")
    public String getOrder(@PathVariable(name = "id") Long id,
                           @RequestParam(name = "newOrder", required = false) String newOrder,
                           Model model) {
        List<Item> items = Arrays.asList(new Item(1L, "title", "description", "imageUrl", 0L, 0L),
                new Item(2L, "title1", "description1", "imageUrl", 0L, 0L),
                new Item(2L, "title2", "description2", "imageUrl", 0L, 0L));
        model.addAttribute("order", new Order(1L, items, 8907L));

        return "order";
    }
}
