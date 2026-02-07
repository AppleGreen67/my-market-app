package ru.yandex.practicum.mymarket.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.reactive.result.view.Rendering;
import reactor.core.publisher.Mono;
import ru.yandex.practicum.mymarket.controller.request.OrderRequest;
import ru.yandex.practicum.mymarket.service.OrderService;
import ru.yandex.practicum.mymarket.service.user.IUserService;

@Controller
@RequestMapping("/orders")
public class OrderController {

    private final IUserService userService;
    private final OrderService orderService;

    public OrderController(IUserService userService, OrderService orderService) {
        this.userService = userService;
        this.orderService = orderService;
    }

    @GetMapping
    public Mono<Rendering> getOrders() {
        return userService.getCurrentUserId()
                .flatMap(userId-> Mono.just(Rendering.view("orders")
                        .modelAttribute("orders", orderService.getOrders(userId))
                        .build()));
    }

    @GetMapping("/{id}")
    public Mono<Rendering> getOrder(@PathVariable(name = "id") Long id,
                                    @ModelAttribute OrderRequest request) {

        return userService.getCurrentUserId()
                .flatMap(userId-> Mono.just(Rendering.view("order")
                .modelAttribute("order", orderService.find(userId, id))
                .build()));
    }
}
