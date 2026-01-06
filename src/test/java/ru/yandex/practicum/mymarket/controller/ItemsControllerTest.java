package ru.yandex.practicum.mymarket.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.mymarket.dto.ItemDto;
import ru.yandex.practicum.mymarket.service.ItemsService;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.clearInvocations;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemsController.class)
class ItemsControllerTest {

    @MockitoBean
    private ItemsService itemsService;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void beforeTest() {
        clearInvocations(itemsService);
    }

    @Test
    void getItems() throws Exception {
        List<ItemDto> items = Arrays.asList(new ItemDto(1L, "title1", "description1", "imageUrl", 11L, 111),
                new ItemDto(2L, "title2", "description2", "imageUrl", 22L, 222),
                new ItemDto(3L, "title3", "description3", "imageUrl", 33L, 333));

        List<ItemDto> items1 = Arrays.asList(new ItemDto(4L, "title4", "description4", "imageUrl", 44L, 444),
                new ItemDto(5L, "title5", "description5", "imageUrl", 55L, 555),
                new ItemDto(6L, "title6", "description6", "imageUrl", 66L, 666));

        when(itemsService.getItems(any(), any(), any(), any()))
                .thenReturn(Arrays.asList(items, items1));

        mockMvc.perform(get("/items"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("/items/1")))
                .andExpect(content().string(containsString("<h5 class=\"card-title\">title1</h5>")))
                .andExpect(content().string(containsString("<p class=\"card-text\">description1</p>")))
                .andExpect(content().string(containsString("11 руб.")))
                .andExpect(content().string(containsString("<span>111</span>")))

                .andExpect(content().string(containsString("/items/6")))
                .andExpect(content().string(containsString("<h5 class=\"card-title\">title6</h5>")))
                .andExpect(content().string(containsString("<p class=\"card-text\">description6</p>")))
                .andExpect(content().string(containsString("66 руб.")))
                .andExpect(content().string(containsString("<span>666</span>")))
                .andExpect(content().contentType("text/html;charset=UTF-8"));

        verify(itemsService).getItems(any(), any(), any(), any());
    }

    @Test
    void getItems_withParams() throws Exception {
        String search = "descr";
        String sort = "NO";
        Integer pageNumber = 2;
        Integer pageSize = 3;

        List<ItemDto> items = Arrays.asList(new ItemDto(1L, "title1", "description1", "imageUrl", 11L, 111),
                new ItemDto(2L, "title2", "description2", "imageUrl", 22L, 222),
                new ItemDto(3L, "title3", "description3", "imageUrl", 33L, 333));

        when(itemsService.getItems(search, sort, pageNumber, pageSize))
                .thenReturn(Arrays.asList(items));

        mockMvc.perform(get("/items?search={search}&sort={sort}&pageNumber={pageNumber}&pageSize={pageSize}",
                        search, sort, pageNumber, pageSize))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("/items/1")))
                .andExpect(content().string(containsString("<h5 class=\"card-title\">title1</h5>")))
                .andExpect(content().string(containsString("<p class=\"card-text\">description1</p>")))
                .andExpect(content().string(containsString("11 руб.")))
                .andExpect(content().string(containsString("<span>111</span>")))
                .andExpect(content().contentType("text/html;charset=UTF-8"));

        verify(itemsService).getItems(search, sort, pageNumber, pageSize);
    }

    @Test
    void getItems_sortIsNo() throws Exception {
        String sort = "NO";

        List<ItemDto> items = Arrays.asList(new ItemDto(1L, "title1", "description1", "imageUrl", 11L, 111),
                new ItemDto(2L, "title2", "description2", "imageUrl", 22L, 222),
                new ItemDto(3L, "title3", "description3", "imageUrl", 33L, 333));

        when(itemsService.getItems(any(), eq(sort), any(), any()))
                .thenReturn(Arrays.asList(items));

        mockMvc.perform(get("/items?sort={sort}", sort))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"));

        verify(itemsService).getItems(any(), eq(sort), any(), any());
    }

    @Test
    void getItems_sortIsALPHA() throws Exception {
        String sort = "ALPHA";

        List<ItemDto> items = Arrays.asList(new ItemDto(1L, "title1", "description1", "imageUrl", 11L, 111),
                new ItemDto(2L, "title2", "description2", "imageUrl", 22L, 222),
                new ItemDto(3L, "title3", "description3", "imageUrl", 33L, 333));

        when(itemsService.getItems(any(), eq(sort), any(), any()))
                .thenReturn(Arrays.asList(items));

        mockMvc.perform(get("/items?sort={sort}", sort))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"));

        verify(itemsService).getItems(any(), eq(sort), any(), any());
    }

    @Test
    void getItems_sortIsPRICE() throws Exception {
        String sort = "PRICE";

        List<ItemDto> items = Arrays.asList(new ItemDto(1L, "title1", "description1", "imageUrl", 11L, 111),
                new ItemDto(2L, "title2", "description2", "imageUrl", 22L, 222),
                new ItemDto(3L, "title3", "description3", "imageUrl", 33L, 333));

        when(itemsService.getItems(any(), eq(sort), any(), any()))
                .thenReturn(Arrays.asList(items));

        mockMvc.perform(get("/items?sort={sort}", sort))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"));

        verify(itemsService).getItems(any(), eq(sort), any(), any());
    }

    @Test
    void getItems_sortIsUnknown() throws Exception {
        String sort = "UNKNOWN";

        try {
            mockMvc.perform(get("/items?sort={sort}", sort))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("text/html;charset=UTF-8"));
            fail();
        } catch (Exception e) {
            assertInstanceOf(UnsupportedOperationException.class, e.getCause());
        }


        verify(itemsService, never()).getItems(any(), eq(sort), any(), any());
    }


    @Test
    void changeCount_plus() throws Exception {
        long id = 1L;
        String action = "PLUS";

        when(itemsService.updateCount(id, action))
                .thenReturn(new ItemDto(1L, "title1", "description1", "imageUrl", 0L, 777));

        mockMvc.perform(post("/items?id={id}&action={action}", id, action))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().stringValues(HttpHeaders.LOCATION, "/items"));

        verify(itemsService).updateCount(id, action);
    }

    @Test
    void changeCount_minus() throws Exception {
        long id = 1L;
        String action = "MINUS";

        when(itemsService.updateCount(id, action))
                .thenReturn(new ItemDto(1L, "title1", "description1", "imageUrl", 0L, 776));

        mockMvc.perform(post("/items?id={id}&action={action}", id, action))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().stringValues(HttpHeaders.LOCATION, "/items"));

        verify(itemsService).updateCount(id, action);
    }

    @Test
    void changeCount_exception() throws Exception {
        long id = 1L;
        String action = "unknow";

        try {
            mockMvc.perform(post("/items?id={id}&action={action}", id, action));
            fail();
        } catch (Exception e) {
            assertInstanceOf(UnsupportedOperationException.class, e.getCause());
        }

        verify(itemsService, never()).updateCount(id, action);
    }

    @Test
    void getItem() throws Exception {
        long id = 1L;

        when(itemsService.find(id))
                .thenReturn(new ItemDto(1L, "title1", "description1", "imageUrl", 0L, 0));

        mockMvc.perform(get("/items/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("/items/1")))
                .andExpect(content().contentType("text/html;charset=UTF-8"));

        verify(itemsService).find(id);
    }

    @Test
    void changeItemCount_plus() throws Exception {
        long id = 1L;
        String action = "PLUS";

        when(itemsService.updateCount(id, action))
                .thenReturn(new ItemDto(1L, "title1", "description1", "imageUrl", 0L, 777));

        mockMvc.perform(post("/items/{id}?action={action}", id, action))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("/items/1")))
                .andExpect(content().string(containsString("<span>777</span>")))
                .andExpect(content().contentType("text/html;charset=UTF-8"));

        verify(itemsService).updateCount(id, action);
    }

    @Test
    void changeItemCount_minus() throws Exception {
        long id = 1L;
        String action = "MINUS";

        when(itemsService.updateCount(id, action))
                .thenReturn(new ItemDto(1L, "title1", "description1", "imageUrl", 0L, 776));

        mockMvc.perform(post("/items/{id}?action={action}", id, action))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("/items/1")))
                .andExpect(content().string(containsString("<span>776</span>")))
                .andExpect(content().contentType("text/html;charset=UTF-8"));

        verify(itemsService).updateCount(id, action);
    }

    @Test
    void changeItemCount_exception() throws Exception {
        long id = 1L;
        String action = "unknow";

        try {
            mockMvc.perform(post("/items/{id}?action={action}", id, action));
            fail();
        } catch (Exception e) {
            assertInstanceOf(UnsupportedOperationException.class, e.getCause());
        }

        verify(itemsService, never()).updateCount(id, action);
    }
}