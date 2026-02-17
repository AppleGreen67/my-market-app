package ru.yandex.practicum.mymarket.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Mono;
import ru.yandex.practicum.mymarket.service.BuyService;
import ru.yandex.practicum.mymarket.service.user.IUserService;

@Controller
@RequestMapping("/buy")
public class BuyController {

    private final BuyService buyService;
    private final IUserService userService;

    public BuyController(BuyService buyService, IUserService userService) {
        this.buyService = buyService;
        this.userService = userService;
    }

    @PostMapping
    public Mono<String> buy() {
        return userService.getCurrentUserId()
                .flatMap(userId -> buyService.buy(userId)
                        .map(orderId->"redirect:/orders/"+orderId+"?newOrder=true"));
    }
}
