package ru.yandex.practicum.mymarket.service.user;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class UserService implements IUserService{
    @Override
    public Mono<Long> getCurrentUserId() {
        return Mono.just(1L);
    }
}
