package ru.yandex.practicum.mymarket.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.mymarket.domain.Cart;
import ru.yandex.practicum.mymarket.domain.Order;
import ru.yandex.practicum.mymarket.domain.OrderItem;
import ru.yandex.practicum.mymarket.repository.CartRepository;
import ru.yandex.practicum.mymarket.repository.OrderRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class BuyService {

    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;

    public BuyService(CartRepository cartRepository, OrderRepository orderRepository) {
        this.cartRepository = cartRepository;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public Long buy(Long userId) {
        Optional<Cart> cartOptional = cartRepository.findByUserId(userId);
        if (cartOptional.isEmpty()) throw new NoSuchElementException();

        Cart cart = cartOptional.get();

        Order order = new Order();

        List<OrderItem> orderItems = cart.getItems().stream()
                .map(cartItem -> {
                    OrderItem orderItem = new OrderItem();
                    orderItem.setItem(cartItem.getItem());
                    orderItem.setCount(cartItem.getCount());
                    orderItem.setOrder(order);
                    return orderItem;
                }).toList();

        order.setOrderItems(orderItems);
        orderRepository.save(order);

        cart.getItems().clear();
        cartRepository.save(cart);

        return order.getId();
    }
}
