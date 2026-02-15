package ru.yandex.practicum.pay;

import org.springframework.stereotype.Service;

@Service
public class BalanceService {

    public Long getBalance(Long userId) {
        return 3000L;
    }
}
