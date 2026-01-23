package ru.yandex.practicum.mymarket.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.result.view.Rendering;
import reactor.core.publisher.Mono;
import ru.yandex.practicum.mymarket.service.OrderService;

@Controller
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public Mono<Rendering> getOrders() {
        return Mono.just(Rendering.view("orders")
                .modelAttribute("orders", orderService.getOrders())
                .build());
    }

    @GetMapping("/{id}")
    public Mono<Rendering> getOrder(@PathVariable(name = "id") Long id,
                           @RequestParam(name = "newOrder", required = false) String newOrder) {
        return Mono.just(Rendering.view("order")
                .modelAttribute("order", orderService.find(id))
                .build());
    }
}
