package ru.yandex.practicum.mymarket.repository;

import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.yandex.practicum.mymarket.domain.CartItem;
import ru.yandex.practicum.mymarket.dto.ItemDto;

@Repository
public class CartDatabaseClientRepository {

    private final DatabaseClient databaseClient;

    public CartDatabaseClientRepository(DatabaseClient databaseClient) {
        this.databaseClient = databaseClient;
    }

    public Flux<ItemDto> findByUserId(Long userId) {
        return databaseClient.sql("""
                        select i.* , ci.item_count as item_count, ci.user_id FROM items i
                        right join cart_items ci on i.id = ci.item_id
                        where ci.user_id = :user_id
                        """)
                .bind("user_id", userId)
                .map((row, metadata) ->
                        new ItemDto(
                                row.get("id", Long.class),
                                row.get("title", String.class),
                                row.get("description", String.class),
                                row.get("img_path", String.class),
                                row.get("price", Long.class),
                                row.get("item_count", Integer.class)
                        ))
                .all().switchIfEmpty(Flux.empty());
    }

    public Mono<ItemDto> findByUserIdAndItemId(Long userId, Long itemId) {
        return databaseClient.sql("""
                        select i.* , ci.item_count as item_count, ci.user_id FROM items i
                        right join cart_items ci on i.id = ci.item_id
                        where ci.user_id = :user_id and i.item_id = :item_id
                        """)
                .bind("user_id", userId)
                .bind("item_id", itemId)
                .map((row, metadata) ->
                        new ItemDto(
                                row.get("id", Long.class),
                                row.get("title", String.class),
                                row.get("description", String.class),
                                row.get("img_path", String.class),
                                row.get("price", Long.class),
                                row.get("item_count", Integer.class)
                        ))
                .one().switchIfEmpty(Mono.empty());
    }

    public void updateItem(Long userId, Long id) {

    }

    public void deleteItem(Long userId, Long id) {

    }

    public Mono<Boolean> saveItem(CartItem cartItem) {
        return null;
    }
}
