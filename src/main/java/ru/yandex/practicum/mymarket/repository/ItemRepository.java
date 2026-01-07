package ru.yandex.practicum.mymarket.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.mymarket.domain.Item;

public interface ItemRepository extends JpaRepository<Item, Long> {

    Page<Item> findAllByTitleContainingOrDescriptionContaining(Pageable pageable, String title, String description);

}
