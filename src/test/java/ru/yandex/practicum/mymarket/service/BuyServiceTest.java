//package ru.yandex.practicum.mymarket.service;
//
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.bean.override.mockito.MockitoBean;
//import reactor.core.publisher.Mono;
//import reactor.test.StepVerifier;
//import ru.yandex.practicum.mymarket.domain.Cart;
//import ru.yandex.practicum.mymarket.domain.CartItem;
//import ru.yandex.practicum.mymarket.domain.Order;
//import ru.yandex.practicum.mymarket.repository.CartRepository;
//import ru.yandex.practicum.mymarket.repository.OrderRepository;
//
//import java.util.NoSuchElementException;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.doAnswer;
//import static org.mockito.Mockito.never;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//import static ru.yandex.practicum.mymarket.utils.ItemsUtils.getItem;
//
//@SpringBootTest(classes = BuyService.class)
//class BuyServiceTest {
//
//    @MockitoBean
//    private CartRepository cartRepository;
//    @MockitoBean
//    private OrderRepository orderRepository;
//
//    @Autowired
//    private BuyService service;
//
//    @Test
//    void buy_noCart_exception() {
//        Long userId = 1L;
//
//        when(cartRepository.findByUserId(userId)).thenReturn(Mono.empty());
//
//        StepVerifier.create(service.buy(userId))
//                .expectError(NoSuchElementException.class)
//                .verify();
//
//        verify(cartRepository).findByUserId(userId);
//        verify(orderRepository, never()).save(any());
//        verify(cartRepository, never()).save(any());
//    }
//
//    @Test
//    void buy() {
//        Long userId = 1L;
//
////        CartItem cartItem = new CartItem();
////        cartItem.setId(1L);
////        cartItem.setItem(getItem(2L, "title2", "description2", "imagePath", 22L));
////        cartItem.setCount(3);
////
////        Cart cart = new Cart();
////        cart.getItems().add(cartItem);
////
////        when(cartRepository.findByUserId(userId)).thenReturn(Mono.just(cart));
//
//        doAnswer(in -> {
//            Order orderInSave = in.getArgument(0);
//            orderInSave.setId(1L);
//
////            assertEquals(1, orderInSave.getOrderItems().size());
////            assertEquals(cartItem.getCount(), orderInSave.getOrderItems().getFirst().getCount());
//
//            return Mono.just(orderInSave);
//        }).when(orderRepository).save(any(Order.class));
//
//        doAnswer(in -> {
//            Cart cartInSave = in.getArgument(0);
//
////            assertEquals(0, cartInSave.getItems().size());
//
//            return Mono.just(cartInSave);
//        }).when(cartRepository).save(any(Cart.class));
//
//        StepVerifier.create(service.buy(userId))
//                .expectNextMatches(orderId -> {
//                    assertEquals(1, orderId);
//                    return true;
//                })
//                .verifyComplete();
//
//        verify(cartRepository).findByUserId(userId);
//    }
//}