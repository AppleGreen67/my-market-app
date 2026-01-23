package ru.yandex.practicum.mymarket.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.result.view.Rendering;
import reactor.core.publisher.Mono;
import ru.yandex.practicum.mymarket.dto.Paging;
import ru.yandex.practicum.mymarket.service.ItemsService;
import ru.yandex.practicum.mymarket.service.user.IUserService;

@Controller
@RequestMapping("/items")
public class ItemsController {

    private final ItemsService itemsService;
    private final IUserService userService;

    public ItemsController(ItemsService itemsService, IUserService userService) {
        this.itemsService = itemsService;
        this.userService = userService;
    }

    @GetMapping
    public Mono<Rendering> getItems(@RequestParam(name = "search", required = false) String search,
                                    @RequestParam(name = "sort", required = false) String sortParam,
                                    @RequestParam(name = "pageNumber", required = false) String number,
                                    @RequestParam(name = "pageSize", required = false) String size) {

        String sort = sortParam == null || sortParam.isEmpty() ? "NO" : sortParam;

        if (!("PRICE".equals(sort) || "ALPHA".equals(sort) || "NO".equals(sort))) {
            throw new UnsupportedOperationException();
        }

        Integer pageNumber = number == null ? 1 : Integer.parseInt(number);
        Integer pageSize = size == null ? 5 : Integer.parseInt(size);

        return userService.getCurrentUserId()
                .flatMap(userId -> Mono.just(
                        Rendering.view("items")
                                .modelAttribute("items", itemsService.getItems(userId, search, sortParam, pageNumber, pageSize))
                                .modelAttribute("paging", new Paging(pageNumber, pageSize))
                                .build()));
    }

    @PostMapping
    public Mono<String> changeCount(@RequestParam(name = "id") Long id,
                                    @RequestParam(name = "search", required = false) String search,
                                    @RequestParam(name = "sort", required = false) String sort,
                                    @RequestParam(name = "pageNumber", required = false) String pageNumber,
                                    @RequestParam(name = "pageSize", required = false) String pageSize,
                                    @RequestParam(name = "action") String action) {

        if (!("MINUS".equals(action) || "PLUS".equals(action))) {
            throw new UnsupportedOperationException();
        }

        return userService.getCurrentUserId()
                .flatMap(userId -> itemsService.updateCountInCart(id, action, userId)
                        .map(itemDto -> "redirect:/items?search=" + search + "&sort=" + sort + "&pageNumber=" + pageNumber + "&pageSize=" + pageSize));
    }

    @GetMapping("/{id}")
    public Mono<Rendering> getItem(@PathVariable(name = "id") Long id) {
        return userService.getCurrentUserId()
                .flatMap(userId -> itemsService.find(id, userId)
                        .map(itemDto -> Rendering.view("item")
                                .modelAttribute("item", itemDto)
                                .build())
                        .switchIfEmpty(Mono.just(Rendering.redirectTo("error").build())));
    }

    @PostMapping("/{id}")
    public Mono<Rendering> changeItemCount(@PathVariable(name = "id") Long id,
                                           @RequestParam(name = "action") String action) {
        if (!("MINUS".equals(action) || "PLUS".equals(action))) {
            throw new UnsupportedOperationException();
        }

        return userService.getCurrentUserId()
                .flatMap(userId -> itemsService.updateCountInCart(id, action, userId)
                        .map(itemDto -> Rendering.view("item")
                                .modelAttribute("item", itemDto)
                                .build()));
    }
}
