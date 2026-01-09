package ru.yandex.practicum.mymarket.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.mymarket.service.BuyService;
import ru.yandex.practicum.mymarket.service.user.IUserService;

import static org.mockito.Mockito.clearInvocations;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BuyController.class)
class BuyControllerTest {

    @MockitoBean
    private BuyService buyService;
    @MockitoBean
    private IUserService userService;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void beforeTest() {
        clearInvocations(buyService);
        clearInvocations(userService);
    }

    @Test
    void buy() throws Exception {
        Long userId = 1L;
        when(userService.getCurrentUserId()).thenReturn(userId);

        when(buyService.buy(userId)).thenReturn(12L);

        mockMvc.perform(post("/buy"))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().stringValues(HttpHeaders.LOCATION, "/orders/12?newOrder=true"));

        verify(userService).getCurrentUserId();
        verify(buyService).buy(userId);
    }
}