package ru.yandex.practicum.mymarket.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.mymarket.dto.ItemDto;
import ru.yandex.practicum.mymarket.service.CartService;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.clearInvocations;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CartController.class)
class CartControllerTest {

    @MockitoBean
    private CartService cartService;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void beforeTest() {
        clearInvocations(cartService);
    }

    @Test
    void getCartItems() throws Exception {
        List<ItemDto> items = Arrays.asList(new ItemDto(1L, "title1", "description1", "imageUrl", 11L, 111),
                new ItemDto(2L, "title2", "description2", "imageUrl", 22L, 222),
                new ItemDto(3L, "title3", "description3", "imageUrl", 33L, 333));

        when(cartService.getCartItems()).thenReturn(items);
        when(cartService.calculateSum(items)).thenReturn(8907L);

        mockMvc.perform(get("/cart/items"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("<h5 class=\"card-title\">title1</h5>")))
                .andExpect(content().string(containsString("<h5 class=\"card-title\">title2</h5>")))
                .andExpect(content().string(containsString("<h5 class=\"card-title\">title3</h5>")))
                .andExpect(content().string(containsString("Итого: 8907 руб.")))
                .andExpect(content().contentType("text/html;charset=UTF-8"));

        verify(cartService).getCartItems();
        verify(cartService).calculateSum(items);
    }

    @Test
    void changeCount_plus() throws Exception {
        long id = 1L;
        String action = "PLUS";

        List<ItemDto> items = Arrays.asList(new ItemDto(1L, "title1", "description1", "imageUrl", 11L, 111),
                new ItemDto(2L, "title2", "description2", "imageUrl", 22L, 222),
                new ItemDto(3L, "title3", "description3", "imageUrl", 33L, 333));

        when(cartService.updateCart(id, action)).thenReturn(items);

        when(cartService.calculateSum(items)).thenReturn(7777L);

        mockMvc.perform(post("/cart/items?id={id}&action={action}", id, action))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("<h5 class=\"card-title\">title1</h5>")))
                .andExpect(content().string(containsString("<h5 class=\"card-title\">title2</h5>")))
                .andExpect(content().string(containsString("<h5 class=\"card-title\">title3</h5>")))
                .andExpect(content().string(containsString("Итого: 7777 руб.")))
                .andExpect(content().contentType("text/html;charset=UTF-8"));

        verify(cartService).updateCart(id, action);
        verify(cartService).calculateSum(items);
    }

    @Test
    void changeCount_minus() throws Exception {
        long id = 1L;
        String action = "MINUS";

        List<ItemDto> items = Arrays.asList(new ItemDto(1L, "title1", "description1", "imageUrl", 11L, 111),
                new ItemDto(2L, "title2", "description2", "imageUrl", 22L, 222),
                new ItemDto(3L, "title3", "description3", "imageUrl", 33L, 333));

        when(cartService.updateCart(id, action)).thenReturn(items);

        when(cartService.calculateSum(items)).thenReturn(666L);

        mockMvc.perform(post("/cart/items?id={id}&action={action}", id, action))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("<h5 class=\"card-title\">title1</h5>")))
                .andExpect(content().string(containsString("<h5 class=\"card-title\">title2</h5>")))
                .andExpect(content().string(containsString("<h5 class=\"card-title\">title3</h5>")))
                .andExpect(content().string(containsString("Итого: 666 руб.")))
                .andExpect(content().contentType("text/html;charset=UTF-8"));

        verify(cartService).updateCart(id, action);
        verify(cartService).calculateSum(items);
    }

    @Test
    void changeCount_delete() throws Exception {
        long id = 1L;
        String action = "DELETE";

        List<ItemDto> items = Arrays.asList(new ItemDto(1L, "title1", "description1", "imageUrl", 11L, 111),
                new ItemDto(3L, "title3", "description3", "imageUrl", 33L, 333));

        when(cartService.updateCart(id, action)).thenReturn(items);

        when(cartService.calculateSum(items)).thenReturn(25L);

        mockMvc.perform(post("/cart/items?id={id}&action={action}", id, action))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("<h5 class=\"card-title\">title1</h5>")))
                .andExpect(content().string(containsString("<h5 class=\"card-title\">title3</h5>")))
                .andExpect(content().string(containsString("Итого: 25 руб.")))
                .andExpect(content().contentType("text/html;charset=UTF-8"));

        verify(cartService).updateCart(id, action);
        verify(cartService).calculateSum(items);
    }

    @Test
    void changeCount_exception() {
        long id = 1L;
        String action = "unknown";
        try {
            mockMvc.perform(post("/cart/items?id={id}&action={action}", id, action))
                    .andExpect(status().isOk())
                    .andExpect(content().string(containsString("Error")))
                    .andExpect(content().string(containsString("Сервис временно недоступен")))
                    .andExpect(content().contentType("text/html;charset=UTF-8"));
        } catch (Exception e) {
            fail();
            assertInstanceOf(UnsupportedOperationException.class, e.getCause());
        }

        verify(cartService, never()).updateCart(id, action);
        verify(cartService, never()).calculateSum(any());
    }
}