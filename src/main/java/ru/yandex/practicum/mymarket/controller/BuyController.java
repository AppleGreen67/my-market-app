package ru.yandex.practicum.mymarket.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.yandex.practicum.mymarket.service.BuyService;

@Controller
@RequestMapping("/buy")
public class BuyController {

    private final BuyService buyService;

    public BuyController(BuyService buyService) {
        this.buyService = buyService;
    }

    @PostMapping
    public String buy(RedirectAttributes redirectAttributes) {
        Long id = buyService.buy();

        redirectAttributes.addAttribute("newOrder", true);
        return "redirect:/orders/"+id;
    }
}
