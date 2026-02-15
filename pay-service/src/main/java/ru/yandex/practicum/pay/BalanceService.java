package ru.yandex.practicum.pay;

import org.springframework.stereotype.Service;

@Service
public class BalanceService {

    public Integer getBalance(Integer userId) {
        return 3000;
    }
}
