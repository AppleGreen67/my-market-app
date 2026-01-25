//package ru.yandex.practicum.mymarket.service;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.bean.override.mockito.MockitoBean;
//import reactor.core.publisher.Flux;
//import reactor.core.publisher.Mono;
//import reactor.test.StepVerifier;
//import ru.yandex.practicum.mymarket.domain.Item;
//import ru.yandex.practicum.mymarket.domain.Order;
//import ru.yandex.practicum.mymarket.domain.OrderItem;
//import ru.yandex.practicum.mymarket.repository.ItemRepository;
//import ru.yandex.practicum.mymarket.repository.OrderItemRepository;
//import ru.yandex.practicum.mymarket.repository.OrderRepository;
//
//import java.util.NoSuchElementException;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyList;
//import static org.mockito.ArgumentMatchers.anySet;
//import static org.mockito.Mockito.clearInvocations;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//import static ru.yandex.practicum.mymarket.utils.ItemsUtils.getItem;
//
//@SpringBootTest(classes = OrderService.class)
//class OrderServiceTest {
//
//    @MockitoBean
//    private SumService sumService;
//    @MockitoBean
//    private OrderRepository orderRepository;
//    @MockitoBean
//    private OrderItemRepository orderItemRepository;
//    @MockitoBean
//    private ItemRepository itemRepository;
//
//    @Autowired
//    private OrderService service;
//
//    @BeforeEach
//    void setUp() {
//        clearInvocations(orderRepository);
//    }
//
//    @Test
//    void getOrders() {
//        Item item = getItem(3L, "title3", "description3", "imagePath", 11L);
//
//        Order order = new Order();
//        order.setId(2L);
//
//        OrderItem orderItem = new OrderItem();
//        orderItem.setId(1L);
//        orderItem.setCount(2);
//        orderItem.setItemId(item.getId());
//        orderItem.setOrderId(order.getId());
//
//
////        when(sumService.calculateSum(any())).thenReturn(22L);
//
//        when(orderRepository.findAll()).thenReturn(Flux.just(order));
//        when(orderItemRepository.findByOrderIdIn(anyList())).thenReturn(Flux.just(orderItem));
//        when(itemRepository.findAllById(anySet())).thenReturn(Flux.just(item));
//
//        StepVerifier.create(service.getOrders())
//                .expectNextMatches(orderDto -> {
//                    assertEquals(2, orderDto.getId());
//                    assertEquals(22, orderDto.getTotalSum());
////                    assertEquals(1, orderDto.getItems().size());
////                    assertEquals(3, orderDto.getItems().getFirst().getId());
////                    assertEquals(2, orderDto.getItems().getFirst().getCount());
//                    return true;
//                })
//                .expectNextCount(0)
//                .verifyComplete();
//
//        verify(orderRepository).findAll();
//    }
//
//    @Test
//    void find_noOrder_exception() {
//        Long id = 2L;
//
//        when(orderRepository.findById(id)).thenReturn(Mono.empty());
//
//        StepVerifier.create(service.find(id))
//                .expectError(NoSuchElementException.class)
//                .verify();
//
//        verify(orderRepository).findById(id);
//    }
//
////    @Test
////    void find() {
////        Long id = 2L;
////
////        OrderItem orderItem = new OrderItem();
////        orderItem.setId(1L);
////        orderItem.setCount(2);
////        orderItem.setItem(getItem(3L, "title3", "description3", "imagePath", 11L));
////
////        Order order = new Order();
////        order.setId(2L);
////
////        orderItem.setOrder(order);
////        order.getOrderItems().add(orderItem);
////
////        when(sumService.calculateSum(any())).thenReturn(22L);
////
////        when(orderRepository.findById(id)).thenReturn(Mono.just(order));
////
////        StepVerifier.create(service.find(id))
////                .expectNextMatches(orderDto -> {
////                    assertEquals(2, orderDto.getId());
////                    assertEquals(22, orderDto.getTotalSum());
////                    assertEquals(1, orderDto.getItems().size());
////                    assertEquals(3, orderDto.getItems().getFirst().getId());
////                    assertEquals(2, orderDto.getItems().getFirst().getCount());
////                    return true;
////                })
////                .verifyComplete();
////
////        verify(orderRepository).findById(id);
////    }
//}