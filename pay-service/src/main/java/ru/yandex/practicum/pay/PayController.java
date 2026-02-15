package ru.yandex.practicum.pay;

import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import ru.yandex.practicum.pay.controller.PaymentApi;
import ru.yandex.practicum.pay.model.PayRequest;
import ru.yandex.practicum.pay.model.UserBalance;

@RestController
public class PayController implements PaymentApi {

    private final BalanceService balanceService;

    public PayController(BalanceService balanceService) {
        this.balanceService = balanceService;
    }

    @Override
    public Mono<ResponseEntity<UserBalance>> payPost(@RequestBody Mono<PayRequest> payRequest, @Parameter(hidden = true) final ServerWebExchange exchange) {

        return payRequest.map(request -> {
            Integer userId = request.getUserId();

            System.out.println("POST pay for user "+ userId);

            UserBalance userBalance = new UserBalance();
            userBalance.setUserId(userId);
            userBalance.setBalance(balanceService.getBalance(userId) - request.getAmount());
            return ResponseEntity.ok().body(userBalance);
        });

    }
}
