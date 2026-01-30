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

    public Mono<Long> saveItem(CartItem cartItem) {
        return databaseClient.sql("""
                        insert into cart_items (user_id, item_id, item_count) values (:user_id, :item_id, :count)
                        """)
                .bind("user_id", cartItem.getUserId())
                .bind("item_id", cartItem.getItemId())
                .bind("count", cartItem.getCount())
                .fetch()
                .rowsUpdated();
    }

    public Mono<Long> updateItem(Long userId, Long itemId, Integer count) {
        return databaseClient.sql("""
                        update cart_items set item_count = :count where user_id = :user_id and item_id = :item_id
                        """)
                .bind("count", count)
                .bind("user_id", userId)
                .bind("item_id", itemId)
                .fetch()
                .rowsUpdated();
    }

    public Mono<Long> deleteItem(Long userId, Long itemId) {
        return databaseClient.sql("""
                        delete from cart_items where item_id = 4 and user_id = 17
                        """)
                .bind("user_id", userId)
                .bind("item_id", itemId)
                .fetch()
                .rowsUpdated();
    }
}
