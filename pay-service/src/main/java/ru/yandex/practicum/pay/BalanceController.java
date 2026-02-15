package ru.yandex.practicum.pay;

import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import ru.yandex.practicum.pay.controller.BalanceApi;
import ru.yandex.practicum.pay.model.UserBalance;

@RestController
public class BalanceController implements BalanceApi {

    private final BalanceService balanceService;

    public BalanceController(BalanceService balanceService) {
        this.balanceService = balanceService;
    }

    @Override
    public Mono<ResponseEntity<UserBalance>> userIdBalanceGet(@PathVariable("userId") Integer userId, @Parameter(hidden = true) final ServerWebExchange exchange) {
        System.out.println("GET balance for user "+userId);

        UserBalance userBalance = new UserBalance();
        userBalance.setUserId(userId);
        userBalance.setBalance(balanceService.getBalance(userId));

        return Mono.just(ResponseEntity.ok().body(userBalance));
    }
}
