package ru.yandex.practicum.mymarket.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
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
    public String buy(RedirectAttributes redirectAttributes) {
        Long userId = userService.getCurrentUserId();

        Long id = buyService.buy(userId);

        redirectAttributes.addAttribute("newOrder", true);
        return "redirect:/orders/"+id;
    }
}
