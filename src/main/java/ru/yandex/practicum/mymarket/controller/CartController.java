package ru.yandex.practicum.mymarket.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.yandex.practicum.mymarket.dto.ItemDto;
import ru.yandex.practicum.mymarket.service.CartService;
import ru.yandex.practicum.mymarket.service.SumService;

import java.util.List;

@Controller
@RequestMapping("/cart/items")
public class CartController {

    private final CartService cartService;
    private final SumService sumService;

    public CartController(CartService cartService, SumService sumService) {
        this.cartService = cartService;
        this.sumService = sumService;
    }

    @GetMapping
    public String getCartItems(Model model) {
        List<ItemDto> items = cartService.getCartItems();
        model.addAttribute("items", items);

        Long totalSum = sumService.calculateSum(items);
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

        List<ItemDto> items = cartService.updateCart(id, action);
        model.addAttribute("items", items);

        Long totalSum = sumService.calculateSum(items);
        model.addAttribute("total", totalSum);
        return "cart";
    }
}
