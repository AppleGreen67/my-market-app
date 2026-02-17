package ru.yandex.practicum.mymarket.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import ru.yandex.practicum.mymarket.exception.PaymentException;

import java.util.NoSuchElementException;

@Service
public class BuyService {

    private final CartService cartService;
    private final OrderService orderService;
    private final SumService sumService;
    private final PayService payService;

    public BuyService(CartService cartService, OrderService orderService, SumService sumService, PayService payService) {
        this.cartService = cartService;
        this.orderService = orderService;
        this.sumService = sumService;
        this.payService = payService;
    }

    @Transactional
    public Mono<Long> buy(Long userId) {
        return cartService.getCartItems(userId)
                .switchIfEmpty(Mono.error(new NoSuchElementException("Корзина не найдена для пользователя: " + userId)))
                .collectList()
                .flatMap(cartItemsList -> {
                    Long sum = sumService.calculateSum(cartItemsList);

                    return payService.pay(userId, sum)
                            .flatMap(payResult ->{
                                if (payResult){
                                    return orderService.create(userId)
                                            .flatMap(orderId -> orderService.saveItems(cartItemsList, orderId)
                                                    .flatMap(l -> cartService.deleteAll(userId))
                                                    .then(Mono.just(orderId)));
                                } else {
                                    return Mono.error(new PaymentException("Не получилось провести оплату. Сумма платежа превышает баланс"));
                                }
                            });
                });
    }
}
