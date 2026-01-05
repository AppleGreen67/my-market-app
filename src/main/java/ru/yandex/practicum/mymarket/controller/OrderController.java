package ru.yandex.practicum.mymarket.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.yandex.practicum.mymarket.dto.Order;
import ru.yandex.practicum.mymarket.service.OrderService;

import java.util.List;

@Controller
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public String getOrders(Model model) {
        List<Order> orders = orderService.getOrders();
        model.addAttribute("orders", orders);
        return "orders";
    }

    @GetMapping("/{id}")
    public String getOrder(@PathVariable(name = "id") Long id,
                           @RequestParam(name = "newOrder", required = false) String newOrder,
                           Model model) {
        Order order = orderService.find(id);
        model.addAttribute("order", order);
        return "order";
    }
}
