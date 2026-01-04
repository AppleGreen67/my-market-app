package ru.yandex.practicum.mymarket.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.yandex.practicum.mymarket.dto.Item;

import java.util.Arrays;

@Controller
@RequestMapping("/cart/items")
public class CartController {

    @GetMapping
    public String getCartItems(Model model) {
        model.addAttribute("items", Arrays.asList(new Item(1L, "title", "description", "imageUrl", 0L, 0L),
                new Item(2L, "title1", "description1", "imageUrl", 0L, 0L),
                new Item(2L, "title2", "description2", "imageUrl", 0L, 0L)));

        model.addAttribute("total", 8907);
        return "cart";
    }

    @PostMapping
    public String changeCount(@RequestParam(name = "id") String id,
                              @RequestParam(name = "action") String action,
                              Model model) {

        //todo обработка вход запроса id/action
        //todo itemsService.updateItemCount(id, action)

        model.addAttribute("items", Arrays.asList(new Item(1L, "title", "description", "imageUrl", 0L, 0L),
                new Item(2L, "title2", "description2", "imageUrl", 0L, 0L)));

        model.addAttribute("total", 8900);
        return "cart";
    }
}
