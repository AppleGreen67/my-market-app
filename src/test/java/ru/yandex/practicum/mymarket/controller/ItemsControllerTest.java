package ru.yandex.practicum.mymarket.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.yandex.practicum.mymarket.dto.ItemDto;
import ru.yandex.practicum.mymarket.service.ItemsService;
import ru.yandex.practicum.mymarket.service.user.IUserService;

import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.clearInvocations;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@WebFluxTest(ItemsController.class)
class ItemsControllerTest {

    @MockitoBean
    private ItemsService itemsService;
    @MockitoBean
    private IUserService userService;

    @Autowired
    private WebTestClient webTestClient;

    @BeforeEach
    void beforeTest() {
        clearInvocations(itemsService);
        clearInvocations(userService);
    }

    @Test
    void getItems() {
        Flux<ItemDto> items = Flux.just(new ItemDto(1L, "title1", "description1", "imageUrl", 11L, 111),
                new ItemDto(2L, "title2", "description2", "imageUrl", 22L, 222),
                new ItemDto(3L, "title3", "description3", "imageUrl", 33L, 333),
                new ItemDto(4L, "title4", "description4", "imageUrl", 44L, 444),
                new ItemDto(5L, "title5", "description5", "imageUrl", 55L, 555),
                new ItemDto(6L, "title6", "description6", "imageUrl", 66L, 666));

        Long userId = 1L;
        when(userService.getCurrentUserId()).thenReturn(Mono.just(userId));

        when(itemsService.getItems(eq(userId), any(), any(), any(), any()))
                .thenReturn(items);

        webTestClient.get()
                .uri("/items")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentTypeCompatibleWith("text/html;charset=UTF-8")
                .expectBody(String.class)
                .value(html -> {
                    assert html.contains("/items/1");
                    assert html.contains("<h5 class=\"card-title\">title1</h5>");
                    assert html.contains("/items/6");
                    assert html.contains("<h5 class=\"card-title\">title6</h5>");
                });

        verify(userService).getCurrentUserId();
        verify(itemsService).getItems(eq(userId), any(), any(), any(), any());
    }

    @Test
    void getItems_withParams() {
        String search = "descr";
        String sort = "NO";
        Integer pageNumber = 2;
        Integer pageSize = 3;

        Flux<ItemDto> items = Flux.just(new ItemDto(1L, "title1", "description1", "imageUrl", 11L, 111),
                new ItemDto(2L, "title2", "description2", "imageUrl", 22L, 222),
                new ItemDto(3L, "title3", "description3", "imageUrl", 33L, 333));

        Long userId = 1L;
        when(userService.getCurrentUserId()).thenReturn(Mono.just(userId));

        when(itemsService.getItems(userId, search, sort, pageNumber, pageSize))
                .thenReturn(items);

        webTestClient.get()
                .uri("/items?search={search}&sort={sort}&pageNumber={pageNumber}&pageSize={pageSize}",
                        search, sort, pageNumber, pageSize)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentTypeCompatibleWith("text/html;charset=UTF-8")
                .expectBody(String.class)
                .value(html -> {
                    assert html.contains("/items/1");
                    assert html.contains("<h5 class=\"card-title\">title1</h5>");
                    assert html.contains("11 руб.");
                    assert html.contains("<span>111</span>");
                });

        verify(userService).getCurrentUserId();
        verify(itemsService).getItems(userId, search, sort, pageNumber, pageSize);
    }

    @Test
    void getItems_sortIsNo() {
        String sort = "NO";

        Flux<ItemDto> items = Flux.just(new ItemDto(1L, "title1", "description1", "imageUrl", 11L, 111),
                new ItemDto(2L, "title2", "description2", "imageUrl", 22L, 222),
                new ItemDto(3L, "title3", "description3", "imageUrl", 33L, 333));

        Long userId = 1L;
        when(userService.getCurrentUserId()).thenReturn(Mono.just(userId));

        when(itemsService.getItems(eq(userId), any(), eq(sort), any(), any()))
                .thenReturn(items);

        webTestClient.get()
                .uri("/items?sort={sort}", sort)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentTypeCompatibleWith("text/html;charset=UTF-8")
                .expectBody(String.class)
                .value(html -> {
                    assert html.contains("/items/1");
                    assert html.contains("<h5 class=\"card-title\">title1</h5>");
                    assert html.contains("11 руб.");
                    assert html.contains("<span>111</span>");
                });

        verify(userService).getCurrentUserId();
        verify(itemsService).getItems(eq(userId), any(), eq(sort), any(), any());
    }

    @Test
    void getItems_sortIsALPHA() {
        String sort = "ALPHA";

        Flux<ItemDto> items = Flux.just(new ItemDto(1L, "title1", "description1", "imageUrl", 11L, 111),
                new ItemDto(2L, "title2", "description2", "imageUrl", 22L, 222),
                new ItemDto(3L, "title3", "description3", "imageUrl", 33L, 333));

        Long userId = 1L;
        when(userService.getCurrentUserId()).thenReturn(Mono.just(userId));

        when(itemsService.getItems(eq(userId), any(), eq(sort), any(), any()))
                .thenReturn(items);

        webTestClient.get()
                .uri("/items?sort={sort}", sort)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentTypeCompatibleWith("text/html;charset=UTF-8")
                .expectBody(String.class)
                .value(html -> {
                    assert html.contains("/items/1");
                    assert html.contains("<h5 class=\"card-title\">title1</h5>");
                    assert html.contains("11 руб.");
                    assert html.contains("<span>111</span>");
                });

        verify(userService).getCurrentUserId();
        verify(itemsService).getItems(eq(userId), any(), eq(sort), any(), any());
    }

    @Test
    void getItems_sortIsPRICE() {
        String sort = "PRICE";

         Flux<ItemDto> items = Flux.just(new ItemDto(1L, "title1", "description1", "imageUrl", 11L, 111),
                new ItemDto(2L, "title2", "description2", "imageUrl", 22L, 222),
                new ItemDto(3L, "title3", "description3", "imageUrl", 33L, 333));

        Long userId = 1L;
        when(userService.getCurrentUserId()).thenReturn(Mono.just(userId));

        when(itemsService.getItems(eq(userId), any(), eq(sort), any(), any()))
                .thenReturn(items);

        webTestClient.get()
                .uri("/items?sort={sort}", sort)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentTypeCompatibleWith("text/html;charset=UTF-8")
                .expectBody(String.class)
                .value(html -> {
                    assert html.contains("/items/1");
                    assert html.contains("<h5 class=\"card-title\">title1</h5>");
                    assert html.contains("11 руб.");
                    assert html.contains("<span>111</span>");
                });

        verify(userService).getCurrentUserId();
        verify(itemsService).getItems(eq(userId), any(), eq(sort), any(), any());
    }

    @Test
    void getItems_sortIsUnknown() {
        String sort = "UNKNOWN";

        try {
            webTestClient.post()
                    .uri("/items?sort={sort}", sort)
                    .exchange()
                    .expectStatus().isOk()
                    .expectHeader().contentTypeCompatibleWith("text/html;charset=UTF-8")
                    .expectBody(String.class)
                    .value(html -> {
                        assert html.contains("Error");
                        assert html.contains("Сервис временно недоступен");
                    });
        } catch (Exception e) {
            fail();
        }

        verify(userService, never()).getCurrentUserId();
        verify(itemsService, never()).getItems(any(), any(), eq(sort), any(), any());
    }

    @Test
    void changeCount_allParams_plus() {
        long id = 1L;
        String action = "PLUS";
        String search = "descr";
        String sort = "NO";
        Integer pageNumber = 2;
        Integer pageSize = 3;

        Long userId = 1L;
        when(userService.getCurrentUserId()).thenReturn(Mono.just(userId));

        ItemDto itemDto = new ItemDto(1L, "title1", "description1", "imageUrl", 0L, 777);
        when(itemsService.updateCountInCart(id, action, userId))
                .thenReturn(Mono.just(itemDto));

        webTestClient.post()
                .uri("/items?id={id}&action={action}&search={search}&sort={sort}&pageNumber={pageNumber}&pageSize={pageSize}",
                        id, action, search, sort, pageNumber, pageSize)
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().location("/items?search=descr&sort=NO&pageNumber=2&pageSize=3");

        verify(userService).getCurrentUserId();
        verify(itemsService).updateCountInCart(id, action, userId);
    }

    @Test
    void changeCount_plus() {
        long id = 1L;
        String action = "PLUS";

        Long userId = 1L;
        when(userService.getCurrentUserId()).thenReturn(Mono.just(userId));

        ItemDto itemDto = new ItemDto(1L, "title1", "description1", "imageUrl", 0L, 777);
        when(itemsService.updateCountInCart(id, action, userId))
                .thenReturn((Mono.just(itemDto)));

        webTestClient.post()
                .uri("/items?id={id}&action={action}", id, action)
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().location("/items?search=null&sort=null&pageNumber=null&pageSize=null");
        ;

        verify(userService).getCurrentUserId();
        verify(itemsService).updateCountInCart(id, action, userId);
    }

    @Test
    void changeCount_minus() {
        long id = 1L;
        String action = "MINUS";

        Long userId = 1L;
        when(userService.getCurrentUserId()).thenReturn(Mono.just(userId));

        ItemDto itemDto = new ItemDto(1L, "title1", "description1", "imageUrl", 0L, 776);
        when(itemsService.updateCountInCart(id, action, userId))
                .thenReturn((Mono.just(itemDto)));

        webTestClient.post()
                .uri("/items?id={id}&action={action}", id, action)
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().location("/items?search=null&sort=null&pageNumber=null&pageSize=null");

        verify(userService).getCurrentUserId();
        verify(itemsService).updateCountInCart(id, action, userId);
    }

    @Test
    void changeCount_exception() {
        long id = 1L;
        String action = "unknow";

        try {
            webTestClient.post()
                    .uri("/items?id={id}&action={action}", id, action)
                    .exchange()
                    .expectStatus().isOk()
                    .expectHeader().contentTypeCompatibleWith("text/html;charset=UTF-8")
                    .expectBody(String.class)
                    .value(html -> {
                        assert html.contains("Error");
                        assert html.contains("Сервис временно недоступен");
                    });
        } catch (Exception e) {
            fail();
        }

        verify(userService, never()).getCurrentUserId();
        verify(itemsService, never()).updateCountInCart(eq(id), eq(action), any());
    }

    @Test
    void getItem() {
        long id = 1L;

        Long userId = 1L;
        when(userService.getCurrentUserId()).thenReturn(Mono.just(userId));

        ItemDto itemDto = new ItemDto(1L, "title1", "description1", "imageUrl", 0L, 0);
        when(itemsService.find(id, userId))
                .thenReturn((Mono.just(itemDto)));

        webTestClient.get()
                .uri("/items/{id}", id)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentTypeCompatibleWith("text/html;charset=UTF-8")
                .expectBody(String.class)
                .value(html -> {
                    assert html.contains("/items/1");
                });

        verify(userService).getCurrentUserId();
        verify(itemsService).find(id, userId);
    }

    @Test
    void changeItemCount_plus() {
        long id = 1L;
        String action = "PLUS";

        Long userId = 1L;
        when(userService.getCurrentUserId()).thenReturn(Mono.just(userId));

        ItemDto itemDto = new ItemDto(1L, "title1", "description1", "imageUrl", 0L, 777);
        when(itemsService.updateCountInCart(id, action, userId))
                .thenReturn((Mono.just(itemDto)));

        webTestClient.post()
                .uri("/items/{id}?action={action}", id, action)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentTypeCompatibleWith("text/html;charset=UTF-8")
                .expectBody(String.class)
                .value(html -> {
                    assert html.contains("/items/1");
                    assert html.contains("<span>777</span>");
                });

        verify(userService).getCurrentUserId();
        verify(itemsService).updateCountInCart(id, action, userId);
    }

    @Test
    void changeItemCount_minus() {
        long id = 1L;
        String action = "MINUS";

        Long userId = 1L;
        when(userService.getCurrentUserId()).thenReturn(Mono.just(userId));

        ItemDto itemDto = new ItemDto(1L, "title1", "description1", "imageUrl", 0L, 776);
        when(itemsService.updateCountInCart(id, action, userId))
                .thenReturn(Mono.just(itemDto));

        webTestClient.post()
                .uri("/items/{id}?action={action}", id, action)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentTypeCompatibleWith("text/html;charset=UTF-8")
                .expectBody(String.class)
                .value(html -> {
                    assert html.contains("/items/1");
                    assert html.contains("<span>776</span>");
                });

        verify(userService).getCurrentUserId();
        verify(itemsService).updateCountInCart(id, action, userId);
    }

    @Test
    void changeItemCount_exception() {
        long id = 1L;
        String action = "unknow";

        try {
            webTestClient.post()
                    .uri("/items/{id}?action={action}", id, action)
                    .exchange()
                    .expectStatus().isOk()
                    .expectHeader().contentTypeCompatibleWith("text/html;charset=UTF-8")
                    .expectBody(String.class)
                    .value(html -> {
                        assert html.contains("Error");
                        assert html.contains("Сервис временно недоступен");
                    });
        } catch (Exception e) {
            fail();
        }

        verify(userService, never()).getCurrentUserId();
        verify(itemsService, never()).updateCountInCart(eq(id), eq(action), any());
    }
}