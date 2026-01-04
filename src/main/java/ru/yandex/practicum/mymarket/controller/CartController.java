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

    private CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    public String getCartItems(Model model) {

        List<Item> items = cartService.getItems();
        model.addAttribute("items", items);

        //todo calculate sum
        model.addAttribute("total", 8907);
        return "cart";
    }

    @PostMapping
    public String changeCount(@RequestParam(name = "id") String id,
                              @RequestParam(name = "action") String action,
                              Model model) {

        //todo обработка вход запроса id/action
        List<Item> items = cartService.updateCount(id, action);
        model.addAttribute("items", items);

        //todo calculate sum
        model.addAttribute("total", 8900);
        return "cart";
    }
}
