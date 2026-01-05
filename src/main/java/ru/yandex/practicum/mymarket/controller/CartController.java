package ru.yandex.practicum.mymarket.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.yandex.practicum.mymarket.dto.Item;
import ru.yandex.practicum.mymarket.service.CartService;

import java.util.List;

@Controller
@RequestMapping("/cart/items")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    public String getCartItems(Model model) {
        List<Item> items = cartService.getItems();
        model.addAttribute("items", items);

        Long totalSum = cartService.calculateSum(items);
        model.addAttribute("total", totalSum);
        return "cart";
    }

    @PostMapping
    public String changeCount(@RequestParam(name = "id") Long id,
                              @RequestParam(name = "action") String action,
                              Model model) {

        if (!("MINUS".equals(action) || "PLUS".equals(action)|| "DELETE".equals(action))) {
            throw new UnsupportedOperationException();
        }

        //todo обработка вход запроса id/action
        List<Item> items = cartService.updateCount(id, action);
        model.addAttribute("items", items);

        Long totalSum = cartService.calculateSum(items);
        model.addAttribute("total", totalSum);
        return "cart";
    }
}
