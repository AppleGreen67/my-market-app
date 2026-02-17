package ru.yandex.practicum.mymarket.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import ru.yandex.practicum.mymarket.cache.ItemsCache;
import ru.yandex.practicum.mymarket.domain.CartItem;
import ru.yandex.practicum.mymarket.dto.ItemDto;
import ru.yandex.practicum.mymarket.repository.CartDatabaseClientRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.clearInvocations;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = CartService.class)
class CartServiceTest {

    @MockitoBean
    private CartDatabaseClientRepository cartDatabaseClientRepository;
    @MockitoBean
    private ItemsCache itemsCache;

    @Autowired
    private CartService service;

    @BeforeEach
    void setUp() {
        clearInvocations(cartDatabaseClientRepository);
        clearInvocations(itemsCache);
    }

    @Test
    void getCartItems() {
        Long userId = 1L;

        ItemDto itemDto1 = new ItemDto();
        itemDto1.setId(2L);
        itemDto1.setCount(12);

        ItemDto itemDto = new ItemDto();
        itemDto.setId(1L);
        itemDto.setTitle("Бейсболка черная");
        itemDto.setCount(1);

        when(cartDatabaseClientRepository.findByUserId(userId)).thenReturn(Flux.just(itemDto, itemDto1));

        StepVerifier.create(service.getCartItems(userId))
                .expectNextCount(2)
                .verifyComplete();

        verify(cartDatabaseClientRepository).findByUserId(userId);
    }

    @Test
    void updateCart_update() {
        Long userId = 1L;

        Long itemId = 1L;
        String action = "PLUS";

        when(itemsCache.findById(itemId)).thenReturn(Mono.just(new ItemDto()));

        ItemDto itemDto = new ItemDto();
        itemDto.setId(itemId);
        itemDto.setTitle("Бейсболка черная");
        itemDto.setCount(1);

        CartItem cartItem = new CartItem();
        cartItem.setId(55L);
        cartItem.setItemId(itemId);
        cartItem.setUserId(userId);
        cartItem.setCount(77);
        when(cartDatabaseClientRepository.findByUserIdAndItemId(userId, itemId)).thenReturn(Mono.just(cartItem));

        doAnswer(invocationOnMock -> {
            itemDto.setCount(invocationOnMock.getArgument(2));
            return Mono.just(1L);
        }).when(cartDatabaseClientRepository).updateItem(eq(userId), eq(itemId), anyInt());

        StepVerifier.create(service.updateCart(itemId, action, userId))
                .expectNextMatches(dto -> {
                    assertEquals(1, dto);
                    return true;
                })
                .verifyComplete();

        verify(cartDatabaseClientRepository).findByUserIdAndItemId(userId, itemId);
        verify(cartDatabaseClientRepository).updateItem(eq(userId), eq(itemId), anyInt());
        verify(cartDatabaseClientRepository, never()).deleteItem(eq(userId), eq(itemId));
        verify(itemsCache).findById(itemId);
        verify(cartDatabaseClientRepository, never()).saveItem(any(CartItem.class));
    }

    @Test
    void updateCart_delete() {
        Long userId = 1L;

        Long itemId = 1L;
        String action = "MINUS";

        when(itemsCache.findById(itemId)).thenReturn(Mono.just(new ItemDto()));

        CartItem cartItem = new CartItem();
        cartItem.setId(55L);
        cartItem.setItemId(itemId);
        cartItem.setUserId(userId);
        cartItem.setCount(1);
        when(cartDatabaseClientRepository.findByUserIdAndItemId(userId, itemId)).thenReturn(Mono.just(cartItem));

        when(cartDatabaseClientRepository.deleteItem(eq(userId), eq(itemId))).thenReturn(Mono.just(1L));

        StepVerifier.create(service.updateCart(itemId, action, userId))
                .expectNextMatches(dto -> {
                    assertEquals(1, dto);
                    return true;
                })
                .verifyComplete();

        verify(cartDatabaseClientRepository).findByUserIdAndItemId(userId, itemId);
        verify(cartDatabaseClientRepository, never()).updateItem(eq(userId), eq(itemId), anyInt());
        verify(cartDatabaseClientRepository).deleteItem(eq(userId), eq(itemId));
        verify(itemsCache).findById(itemId);
        verify(cartDatabaseClientRepository, never()).saveItem(any(CartItem.class));
    }

    @Test
    void updateCart_noCartItem() {
        Long userId = 1L;

        Long itemId = 1L;
        String action = "PLUS";

        when(cartDatabaseClientRepository.findByUserIdAndItemId(userId, itemId)).thenReturn(Mono.empty());

        ItemDto foundedItem = new ItemDto();
        foundedItem.setId(itemId);
        foundedItem.setTitle("Бейсболка черная");
        foundedItem.setDescription("Очень модная бейсболка черного цвета");
        foundedItem.setImgPath("2.jpg");
        foundedItem.setPrice(1500L);
        when(itemsCache.findById(itemId)).thenReturn(Mono.just(foundedItem));

        ItemDto itemDto1 = new ItemDto();
        itemDto1.setId(2L);
        itemDto1.setCount(12);

        ItemDto itemDto = new ItemDto();
        itemDto.setId(1L);

        when(cartDatabaseClientRepository.findByUserId(userId)).thenReturn(Flux.just(itemDto, itemDto1));

        doAnswer(invocationOnMock -> {
            CartItem cartItem = invocationOnMock.getArgument(0);
            assertEquals(userId, cartItem.getUserId());
            assertEquals(itemId, cartItem.getItemId());
            assertEquals(1, cartItem.getCount());
            return Mono.just(1L);
        }).when(cartDatabaseClientRepository).saveItem(any());

        StepVerifier.create(service.updateCart(itemId, action, userId))
                .expectNextMatches(dto -> {
                    assertEquals(1, dto);
                    return true;
                })
                .verifyComplete();

        verify(cartDatabaseClientRepository).findByUserIdAndItemId(userId, itemId);
        verify(cartDatabaseClientRepository, never()).updateItem(eq(userId), eq(itemId), anyInt());
        verify(cartDatabaseClientRepository, never()).deleteItem(eq(userId), eq(itemId));
        verify(itemsCache).findById(itemId);
        verify(cartDatabaseClientRepository).saveItem(any(CartItem.class));
    }

}