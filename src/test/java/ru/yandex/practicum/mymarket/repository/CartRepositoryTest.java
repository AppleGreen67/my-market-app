//package ru.yandex.practicum.mymarket.repository;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
//import reactor.test.StepVerifier;
//import ru.yandex.practicum.mymarket.domain.Cart;
//import ru.yandex.practicum.mymarket.domain.CartItem;
//import ru.yandex.practicum.mymarket.domain.Item;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static ru.yandex.practicum.mymarket.utils.ItemsUtils.getItem;
//
//@DataR2dbcTest
//class CartRepositoryTest {
//
//    @Autowired
//    private ItemRepository itemRepository;
//    @Autowired
//    private CartItemRepository cartItemRepository;
//    @Autowired
//    private CartRepository cartRepository;
//
//    @BeforeEach
//    void setUp() {
//        itemRepository.deleteAll();
//        cartItemRepository.deleteAll();
//        cartRepository.deleteAll();
//    }
//
//    @Test
//    void findByUserId_noCart() {
//        Long userId = 1L;
//
//        StepVerifier.create(cartRepository.findByUserId(userId))
//                .expectNextCount(0)
//                .verifyComplete();
//    }
//
//    @Test
//    void findByUserId() {
//        Long userId = 1L;
//
//        Item item = getItem("title2", "description2", "imagePath", 22L);
//        StepVerifier.create(itemRepository.save(item))
//                .expectNextMatches(savedItem -> {
//                    assertNotNull(item.getId());
//                    return true;
//                })
//                .verifyComplete();
//
//        Cart cart = new Cart();
//        cart.setUserId(userId);
//        StepVerifier.create(cartRepository.save(cart))
//                .expectNextMatches(savedCar -> {
//                    assertNotNull(savedCar.getId());
//                    return true;
//                })
//                .verifyComplete();
//
//
//        CartItem cartItem = new CartItem();
////        cartItem.setCartId(cart.getId());
//        cartItem.setItemId(item.getId());
////        cartItem.setItemTitle("title2");
//        cartItem.setCount(3);
//        StepVerifier.create(cartItemRepository.save(cartItem))
//                .expectNextMatches(savedCarItem -> {
//                    assertNotNull(savedCarItem.getId());
//                    return true;
//                })
//                .verifyComplete();
//
//        StepVerifier.create(cartRepository.findByUserId(userId))
//                .expectNextMatches(foundedCar -> {
//                    assertNotNull(foundedCar.getId());
//                    return true;
//                })
//                .verifyComplete();
//
//        StepVerifier.create(cartItemRepository.save(cartItem))
//                .expectNextMatches(savedCarItem -> {
//                    assertNotNull(savedCarItem.getId());
//                    return true;
//                })
//                .verifyComplete();
//
//        StepVerifier.create(cartItemRepository.findByCartId(cart.getId()))
//                .expectNextMatches(foundedCarItem -> {
//                    assertNotNull(foundedCarItem.getId());
//                    assertEquals(item.getId(), foundedCarItem.getItemId());
////                    assertEquals(cart.getId(), foundedCarItem.getCartId());
////                    assertEquals("title2", foundedCarItem.getItemTitle());
//                    assertEquals(3, foundedCarItem.getCount());
//                    return true;
//                })
//                .verifyComplete();
//    }
//}