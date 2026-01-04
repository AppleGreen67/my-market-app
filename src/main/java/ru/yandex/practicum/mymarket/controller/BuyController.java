package ru.yandex.practicum.mymarket.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/buy")
public class BuyController {

    @PostMapping
    public String buy(RedirectAttributes redirectAttributes) {

        //todo обработка вход запроса id/action
        //todo itemsService.updateItemCount(id, action)

        Long id = 15L;
        //todo id = buyService.buy();

        redirectAttributes.addAttribute("newOrder", true);
        return "redirect:/orders/"+id;
    }
}
