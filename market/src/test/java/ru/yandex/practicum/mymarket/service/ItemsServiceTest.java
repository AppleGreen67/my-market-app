package ru.yandex.practicum.mymarket.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import ru.yandex.practicum.mymarket.cache.ItemsCache;
import ru.yandex.practicum.mymarket.domain.CartItem;
import ru.yandex.practicum.mymarket.dto.ItemDto;
import ru.yandex.practicum.mymarket.repository.CartDatabaseClientRepository;
import ru.yandex.practicum.mymarket.repository.ItemDatabaseClientRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.clearInvocations;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = ItemsService.class)
class ItemsServiceTest {

    @MockitoBean
    private ItemsCache itemsCache;
    @MockitoBean
    private CartService cartService;
    @MockitoBean
    private CartDatabaseClientRepository cartRepository;

    @Autowired
    private ItemsService service;

    @BeforeEach
    void setUp() {
        clearInvocations(cartService);
        clearInvocations(itemsCache);
        clearInvocations(cartRepository);
    }

    @Test
    public void findById() {
        Long itemId = 1L;
        Long userId = 17L;

        ItemDto itemDto = new ItemDto();
        itemDto.setId(itemId);
        itemDto.setTitle("Бейсболка черная");
        itemDto.setDescription("Очень модная бейсболка черного цвета");
        itemDto.setImgPath("2.jpg");
        itemDto.setPrice(1500L);
        when(itemsCache.findById(itemId)).thenReturn(Mono.just(itemDto));


        CartItem cartItem = new CartItem();
        cartItem.setId(55L);
        cartItem.setItemId(itemId);
        cartItem.setUserId(userId);
        cartItem.setCount(77);
        when(cartRepository.findItem(userId, itemId)).thenReturn(Mono.just(cartItem));

        StepVerifier.create(service.find(itemId, 17L))
                .expectNextMatches(foundedItem ->{
                    assertEquals(1L, foundedItem.getId());
                    assertEquals("Бейсболка черная", foundedItem.getTitle());
                    assertEquals("Очень модная бейсболка черного цвета", foundedItem.getDescription());
                    assertEquals("2.jpg", foundedItem.getImgPath());
                    assertEquals(1500, foundedItem.getPrice());
                    assertEquals(77, foundedItem.getCount());
                    return true;
                })
                .verifyComplete();
    }


    @Test
    public void updateCountInCart() {
        Long itemId = 1L;
        String action = "PLUS";
        Long userId = 17L;
        when(cartService.updateCart(itemId, action, userId)).thenReturn(Mono.just(1L));

        StepVerifier.create(service.updateCountInCart(itemId, action, userId))
                .expectNextMatches(dto ->{
                    assertEquals(1L, dto);
                    return true;
                })
                .verifyComplete();
    }

}