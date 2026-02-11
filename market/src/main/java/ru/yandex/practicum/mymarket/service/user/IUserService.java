package ru.yandex.practicum.mymarket.service.user;

import reactor.core.publisher.Mono;

public interface IUserService {

    Mono<Long> getCurrentUserId();
}
