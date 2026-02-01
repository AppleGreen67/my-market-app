package ru.yandex.practicum.mymarket.repository;

import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.yandex.practicum.mymarket.dto.ItemDto;

@Repository
public class ItemDatabaseClientRepository {

    private final DatabaseClient databaseClient;

    public ItemDatabaseClientRepository(DatabaseClient databaseClient) {
        this.databaseClient = databaseClient;
    }

    public Flux<ItemDto> findAll(String search) {
        StringBuilder sb = new StringBuilder()
                .append("""
                        select i.*, coalesce(ci.item_count, 0) as item_count from items i
                        left join cart_items ci on ci.item_id = i.id
                        """);
        if (search!= null && !search.isBlank())
            sb.append("where title like '%%%s%%' or description like '%%%s%%'".formatted(search, search));

        return databaseClient.sql(sb.toString())
                .map((row, metadata) ->
                        new ItemDto(
                                row.get("id", Long.class),
                                row.get("title", String.class),
                                row.get("description", String.class),
                                row.get("img_path", String.class),
                                row.get("price", Long.class),
                                row.get("item_count", Integer.class)
                        ))
                .all();
    }

    public Flux<ItemDto> findAll() {
        return databaseClient.sql("""
                        select i.*, coalesce(ci.item_count, 0) as item_count from items i
                        left join cart_items ci on ci.item_id = i.id
                        """)
                .map((row, metadata) ->
                        new ItemDto(
                                row.get("id", Long.class),
                                row.get("title", String.class),
                                row.get("description", String.class),
                                row.get("img_path", String.class),
                                row.get("price", Long.class),
                                row.get("item_count", Integer.class)
                        ))
                .all();
    }

    public Mono<ItemDto> findById(Long itemId) {
        return databaseClient.sql("""
                        select i.*, coalesce(ci.item_count, 0) as item_count from items i
                        left join cart_items ci on ci.item_id = i.id
                        where i.id=:id
                        """)
                .bind("id", itemId)
                .map((row, metadata) -> new ItemDto(
                        row.get("id", Long.class),
                        row.get("title", String.class),
                        row.get("description", String.class),
                        row.get("img_path", String.class),
                        row.get("price", Long.class),
                        row.get("item_count", Integer.class)
                ))
                .one();
    }
}
