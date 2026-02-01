package ru.yandex.practicum.mymarket.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.reactive.result.view.Rendering;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.yandex.practicum.mymarket.controller.request.ChangeCountRequest;
import ru.yandex.practicum.mymarket.dto.ItemDto;
import ru.yandex.practicum.mymarket.service.CartService;
import ru.yandex.practicum.mymarket.service.SumService;
import ru.yandex.practicum.mymarket.service.user.IUserService;

@Controller
@RequestMapping("/cart/items")
public class CartController {

    private final CartService cartService;
    private final IUserService userService;
    private final SumService sumService;

    public CartController(CartService cartService, IUserService userService, SumService sumService) {
        this.cartService = cartService;
        this.userService = userService;
        this.sumService = sumService;
    }

    @GetMapping
    public Mono<Rendering> getCartItems() {
        return userService.getCurrentUserId()
                .flatMap(userId -> {
                    Flux<ItemDto> itemDtoFlux = cartService.getCartItems(userId);

                    return Mono.just(Rendering.view("cart")
                            .modelAttribute("items", itemDtoFlux)
                            .modelAttribute("total", sumService.calculateSum(itemDtoFlux))
                            .build());
                });
    }

    @PostMapping
    public Mono<Rendering> changeCount(@ModelAttribute ChangeCountRequest request) {
        String action = request.getAction();
        if (!("MINUS".equals(action) || "PLUS".equals(action) || "DELETE".equals(action))) {
            throw new UnsupportedOperationException();
        }

        return userService.getCurrentUserId()
                .flatMap(userId -> {
                            Flux<ItemDto> itemDtoFlux = cartService.updateCart(request.getId(), action, userId).cache();
                            return Mono.just(Rendering.view("cart")
                                    .modelAttribute("total", sumService.calculateSum(itemDtoFlux))
                                    .modelAttribute("items", itemDtoFlux)
                                    .build());
                        }
                );
    }
}
