//package ru.yandex.practicum.mymarket.repository;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
//import reactor.test.StepVerifier;
//import ru.yandex.practicum.mymarket.domain.Item;
//import ru.yandex.practicum.mymarket.domain.Order;
//import ru.yandex.practicum.mymarket.domain.OrderItem;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static ru.yandex.practicum.mymarket.utils.ItemsUtils.getItem;
//
//@DataR2dbcTest
//class OrderRepositoryTest {
//
//    @Autowired
//    private ItemRepository itemRepository;
//    @Autowired
//    private OrderRepository repository;
//
//    @BeforeEach
//    void setUp() {
//        itemRepository.deleteAll();
//        repository.deleteAll();
//    }
//
//    @Test
//    void findAll() {
//        Item item = getItem("title2", "description2", "imagePath", 22L);
////        itemRepository.save(item);
//
//        OrderItem orderItem = new OrderItem();
//        orderItem.setItemId(1L);
//        orderItem.setCount(2);
//
//        Order order = new Order();
////        orderItem.setOrder(order);
////        order.getOrderItems().add(orderItem);
//
//        repository.save(order).block();
//
//        StepVerifier.create(repository.findAll())
//                .expectNextMatches(savedOrder -> {
//                    assertNotNull(savedOrder.getId());
////                    assertEquals(1, savedOrder.getOrderItems().size());
////                    assertNotNull(savedOrder.getOrderItems().getFirst().getId());
////                    assertEquals(2, savedOrder.getOrderItems().getFirst().getCount());
//                    return true;
//                })
//                .expectNextCount(0)
//                .verifyComplete();
//    }
//}