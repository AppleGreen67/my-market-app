package ru.yandex.practicum.mymarket.repository;

import org.springframework.data.repository.CrudRepository;
import ru.yandex.practicum.mymarket.domain.Cart;

import java.util.Optional;

public interface CartRepository extends CrudRepository<Cart, Long> {

    Optional<Cart> findByUserId(Long userId);
}
