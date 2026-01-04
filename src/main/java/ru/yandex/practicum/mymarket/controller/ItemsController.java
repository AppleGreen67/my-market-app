package ru.yandex.practicum.mymarket.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.yandex.practicum.mymarket.dto.Item;
import ru.yandex.practicum.mymarket.dto.Paging;

import java.util.Arrays;

@Controller
@RequestMapping("/items")
public class ItemsController {

//    @GetMapping("/")
    @GetMapping
    public String getItems(@RequestParam(name = "search", required = false) String search,
                           @RequestParam(name = "sort", required = false) String sort,
                           @RequestParam(name = "pageNumber", required = false) String pageNumber,
                           @RequestParam(name = "pageSize", required = false) String pageSize,
                           Model model) {
        //todo обработка вход запроса

        model.addAttribute("items", Arrays.asList(new Item(1L, "title", "description", "imageUrl", 0L, 0L),
                new Item(2L, "title1", "description1", "imageUrl", 0L, 0L),
                new Item(2L, "title2", "description2", "imageUrl", 0L, 0L)));

        model.addAttribute("paging", new Paging(Integer.valueOf(pageNumber), Integer.valueOf(pageSize)));
        return "items";
    }

    @PostMapping
    public String changeCount(@RequestParam(name = "id") String id,
                              @RequestParam(name = "search", required = false) String search,
                           @RequestParam(name = "sort", required = false) String sort,
                           @RequestParam(name = "pageNumber", required = false) String pageNumber,
                           @RequestParam(name = "pageSize", required = false) String pageSize,
                           @RequestParam(name = "action") String action,
                            RedirectAttributes redirectAttributes) {

        //todo обработка вход запроса id/action
        //todo itemsService.updateItemCount(id, action)

        redirectAttributes.addAttribute("search", search);
        redirectAttributes.addAttribute("sort", sort);
        redirectAttributes.addAttribute("pageNumber", pageNumber);
        redirectAttributes.addAttribute("pageSize", pageSize);
        return "redirect:/items";
    }

    @GetMapping("/{id}")
    public String getItem(@PathVariable(name = "id") Long id, Model model) {
        Item item = new Item(2L, "title1", "description1", "imageUrl", 0L, 0L);
        model.addAttribute("item", item);
        return "item";
    }

    @PostMapping("/{id}")
    public String changeItemCount(@PathVariable(name = "id") Long id,
                          @RequestParam(name = "action") String action,
                          Model model) {
        Item item = new Item(2L, "title1", "description1", "imageUrl", 0L, 0L);
        model.addAttribute("item", item);
        return "item";
    }
}
