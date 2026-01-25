//package ru.yandex.practicum.mymarket.service;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.bean.override.mockito.MockitoBean;
//import reactor.core.publisher.Mono;
//import reactor.test.StepVerifier;
//import ru.yandex.practicum.mymarket.domain.Cart;
//import ru.yandex.practicum.mymarket.domain.CartItem;
//import ru.yandex.practicum.mymarket.domain.Item;
//import ru.yandex.practicum.mymarket.dto.ItemDto;
//import ru.yandex.practicum.mymarket.repository.CartRepository;
//import ru.yandex.practicum.mymarket.repository.ItemRepository;
//
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertFalse;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.clearInvocations;
//import static org.mockito.Mockito.never;
//import static org.mockito.Mockito.times;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//import static ru.yandex.practicum.mymarket.utils.ItemsUtils.getItem;
//
//@SpringBootTest(classes = CartService.class)
//class CartServiceTest {
//
//    @MockitoBean
//    private CartRepository cartRepository;
//    @MockitoBean
//    private ItemRepository itemRepository;
//
//    @Autowired
//    private CartService service;
//
//    @BeforeEach
//    void setUp() {
//        clearInvocations(cartRepository);
//        clearInvocations(itemRepository);
//    }
//
//    @Test
//    void getCartItems_noCart() {
//        Long userId = 1L;
//
//        when(cartRepository.findByUserId(userId)).thenReturn(Mono.empty());
//
//        StepVerifier.create(service.getCartItems(userId))
//                .expectNextCount(0)
//                .verifyComplete();
//    }
//
//    @Test
//    void getCartItems() {
//        Long userId = 1L;
//
//        CartItem cartItem = new CartItem();
//        cartItem.setId(1L);
////        cartItem.setItem(getItem(2L, "title2", "description2", "imagePath", 22L));
//        cartItem.setCount(3);
//
//        Cart cart = new Cart();
////        cart.getItems().add(cartItem);
//
//        when(cartRepository.findByUserId(userId)).thenReturn(Mono.just(cart));
//
//        StepVerifier.create(service.getCartItems(userId))
//                .expectNextMatches(itemDto -> {
//                    assertEquals(2L, itemDto.getId());
//                    assertEquals("title2", itemDto.getTitle());
//                    assertEquals("description2", itemDto.getDescription());
//                    assertEquals("imagePath", itemDto.getImgPath());
//                    assertEquals(3, itemDto.getCount());
//                    return true;
//                })
//                .expectNextCount(0)
//                .verifyComplete();
//    }
//
////    @Test
////    void updateCart_noCart() {
////        Long userId = 1L;
////
////        Long id = 1L;
////        String action = "PLUS";
////
////        when(cartRepository.findByUserId(userId)).thenReturn(Optional.empty());
////
////        Item item = getItem(1L, "title1", "description1", "imagePath", 4);
////        when(itemRepository.findById(id)).thenReturn(Optional.of(item));
////
////
////        List<ItemDto> itemDtoList = service.updateCart(id, action, userId);
////        assertEquals(1, itemDtoList.size());
////        assertEquals(1, itemDtoList.getFirst().getId());
////        assertEquals(1, itemDtoList.getFirst().getCount());
////
////        verify(cartRepository).findByUserId(userId);
////        verify(itemRepository).findById(id);
////        verify(cartRepository, times(2)).save(any(Cart.class));
////    }
////
////    @Test
////    void updateCart_noCartItem() {
////        Long userId = 1L;
////
////        Long id = 1L;
////        String action = "PLUS";
////
////        CartItem cartItem = new CartItem();
////        cartItem.setId(1L);
////        cartItem.setItem(getItem(2L, "title2", "description2", "imagePath", 22L));
////        cartItem.setCount(3);
////
////        Cart cart = new Cart();
////        cart.getItems().add(cartItem);
////
////        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));
////
////        Item item = getItem(1L, "title1", "description1", "imagePath", 4);
////        when(itemRepository.findById(id)).thenReturn(Optional.of(item));
////
////
////        List<ItemDto> itemDtoList = service.updateCart(id, action, userId);
////        assertEquals(2, itemDtoList.size());
////        assertEquals(2, itemDtoList.get(0).getId());
////        assertEquals(3, itemDtoList.get(0).getCount());
////
////        assertEquals(1, itemDtoList.get(1).getId());
////        assertEquals(1, itemDtoList.get(1).getCount());
////
////        verify(cartRepository).findByUserId(userId);
////        verify(itemRepository).findById(id);
////        verify(cartRepository, times(1)).save(any(Cart.class));
////    }
////
////    @Test
////    void updateCart_plus() {
////        Long userId = 1L;
////
////        Long id = 2L;
////        String action = "PLUS";
////
////        CartItem cartItem = new CartItem();
////        cartItem.setId(1L);
////        cartItem.setItem(getItem(2L, "title2", "description2", "imagePath", 22L));
////        cartItem.setCount(3);
////
////        Cart cart = new Cart();
////        cart.getItems().add(cartItem);
////
////        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));
////
////        List<ItemDto> itemDtoList = service.updateCart(id, action, userId);
////        assertEquals(1, itemDtoList.size());
////        assertEquals(2, itemDtoList.getFirst().getId());
////        assertEquals(4, itemDtoList.getFirst().getCount());
////
////        verify(cartRepository).findByUserId(userId);
////        verify(itemRepository, never()).findById(id);
////        verify(cartRepository, times(1)).save(any(Cart.class));
////    }
////
////    @Test
////    void updateCart_minus() {
////        Long userId = 1L;
////
////        Long id = 2L;
////        String action = "MINUS";
////
////        CartItem cartItem = new CartItem();
////        cartItem.setId(1L);
////        cartItem.setItem(getItem(2L, "title2", "description2", "imagePath", 22L));
////        cartItem.setCount(3);
////
////        Cart cart = new Cart();
////        cart.getItems().add(cartItem);
////
////        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));
////
////        List<ItemDto> itemDtoList = service.updateCart(id, action, userId);
////        assertEquals(1, itemDtoList.size());
////        assertEquals(2, itemDtoList.getFirst().getId());
////        assertEquals(2, itemDtoList.getFirst().getCount());
////
////        verify(cartRepository).findByUserId(userId);
////        verify(itemRepository, never()).findById(id);
////        verify(cartRepository, times(1)).save(any(Cart.class));
////    }
////
////    @Test
////    void updateCart_delete() {
////        Long userId = 1L;
////
////        Long id = 2L;
////        String action = "DELETE";
////
////        CartItem cartItem = new CartItem();
////        cartItem.setId(1L);
////        cartItem.setItem(getItem(2L, "title2", "description2", "imagePath", 22L));
////        cartItem.setCount(3);
////
////        Cart cart = new Cart();
////        cart.getItems().add(cartItem);
////
////        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));
////
////        List<ItemDto> itemDtoList = service.updateCart(id, action, userId);
////        assertEquals(0, itemDtoList.size());
////
////        verify(cartRepository).findByUserId(userId);
////        verify(itemRepository, never()).findById(id);
////        verify(cartRepository, times(1)).save(any(Cart.class));
////    }
////
////    @Test
////    void findCartItemByItem() {
////        CartItem cartItem = new CartItem();
////        cartItem.setId(1L);
////        cartItem.setItem(getItem(2L, "title2", "description2", "imagePath", 2L));
////        cartItem.setCount(3);
////
////        CartItem cartItem2 = new CartItem();
////        cartItem2.setId(2L);
////        cartItem2.setItem(getItem(3L, "title3", "description3", "imagePath", 11L));
////        cartItem2.setCount(3);
////
////        Cart cart = new Cart();
////        cart.getItems().add(cartItem);
////        cart.getItems().add(cartItem2);
////
////
////        Optional<CartItem> cartItemByItem = service.findCartItemByItem(cart, 2L);
////        assertTrue(cartItemByItem.isPresent());
////        assertEquals(2L, cartItemByItem.get().getItem().getId());
////
////
////        cartItemByItem = service.findCartItemByItem(cart, 4L);
////        assertFalse(cartItemByItem.isPresent());
////    }
//
//}