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
import ru.yandex.practicum.mymarket.service.ItemsService;

import java.util.List;

@Controller
@RequestMapping("/items")
public class ItemsController {

    private ItemsService itemsService;

    public ItemsController(ItemsService itemsService) {
        this.itemsService = itemsService;
    }

    //    @GetMapping("/")
    @GetMapping
    public String getItems(@RequestParam(name = "search", required = false) String search,
                           @RequestParam(name = "sort", required = false) String sort,
                           @RequestParam(name = "pageNumber", required = false) String pageNumber,
                           @RequestParam(name = "pageSize", required = false) String pageSize,
                           Model model) {
        if (sort == null) sort = "NO";
        if (!("PRICE".equals(sort) || "ALPHA".equals(sort) || "NO".equals(sort))) {
            throw new UnsupportedOperationException();
        }

        List<List<Item>> itemsList = itemsService.getItems(search, sort, pageNumber, pageSize);
        model.addAttribute("items", itemsList);

        model.addAttribute("paging",
                new Paging(pageNumber == null ? 1 : Integer.valueOf(pageNumber), pageSize == null ? 5 : Integer.valueOf(pageSize)));
        return "items";
    }

    @PostMapping
    public String changeCount(@RequestParam(name = "id") Long id,
                              @RequestParam(name = "search", required = false) String search,
                              @RequestParam(name = "sort", required = false) String sort,
                              @RequestParam(name = "pageNumber", required = false) String pageNumber,
                              @RequestParam(name = "pageSize", required = false) String pageSize,
                              @RequestParam(name = "action") String action,
                              RedirectAttributes redirectAttributes) {

        if (!("MINUS".equals(action) || "PLUS".equals(action))) {
            throw new UnsupportedOperationException();
        }

        itemsService.updateCount(id, action);

        redirectAttributes.addAttribute("search", search);
        redirectAttributes.addAttribute("sort", sort);
        redirectAttributes.addAttribute("pageNumber", pageNumber);
        redirectAttributes.addAttribute("pageSize", pageSize);
        return "redirect:/items";
    }

    @GetMapping("/{id}")
    public String getItem(@PathVariable(name = "id") Long id, Model model) {
        Item item = itemsService.find(id);
        model.addAttribute("item", item);
        return "item";
    }

    @PostMapping("/{id}")
    public String changeItemCount(@PathVariable(name = "id") Long id,
                                  @RequestParam(name = "action") String action,
                                  Model model) {
        if (!("MINUS".equals(action) || "PLUS".equals(action))) {
            throw new UnsupportedOperationException();
        }

        Item item = itemsService.updateCount(id, action);
        model.addAttribute("item", item);
        return "item";
    }
}
