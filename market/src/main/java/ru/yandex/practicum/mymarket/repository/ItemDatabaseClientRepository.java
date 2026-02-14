package ru.yandex.practicum.mymarket.repository;

import org.springframework.cache.annotation.Cacheable;
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

    public Flux<ItemDto> findAll(String search, String sort, Integer pageNumber, Integer pageSize) {
        StringBuilder sb = new StringBuilder()
                .append("""
                        select i.* from items i
                        """);
        if (search!= null && !search.isBlank())
            sb.append("where title like '%%%s%%' or description like '%%%s%%'".formatted(search, search));
        if ("ALPHA".equals(sort))
            sb.append("order by i.title");
        else if ("PRICE".equals(sort))
            sb.append("order by i.price");

        sb.append("""
                 limit %d
                offset %d
                """.formatted(pageSize,  (pageNumber - 1) * pageSize));

        return databaseClient.sql(sb.toString())
                .map((row, metadata) ->
                        new ItemDto(
                                row.get("id", Long.class),
                                row.get("title", String.class),
                                row.get("description", String.class),
                                row.get("img_path", String.class),
                                row.get("price", Long.class),
                                0
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

    @Cacheable(value = "item",key = "#id")
    public Mono<ItemDto> findById(Long id) {
        System.out.println("findById from ItemDatabaseClientRepository");
        return databaseClient.sql("""
                        select i.* from items i
                        where i.id=:id
                        """)
                .bind("id", id)
                .map((row, metadata) -> {
                    ItemDto itemDto = new ItemDto();
                    itemDto.setId(row.get("id", Long.class));
                    itemDto.setTitle(row.get("title", String.class));
                    itemDto.setDescription(row.get("description", String.class));
                    itemDto.setImgPath(row.get("img_path", String.class));
                    itemDto.setPrice(row.get("price", Long.class));
                    itemDto.setCount(0);
                    return itemDto;
                })
                .one();
    }
}
