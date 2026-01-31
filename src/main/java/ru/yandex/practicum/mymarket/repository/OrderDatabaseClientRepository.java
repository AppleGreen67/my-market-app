package ru.yandex.practicum.mymarket.repository;

import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.yandex.practicum.mymarket.domain.Order;
import ru.yandex.practicum.mymarket.domain.OrderItem;
import ru.yandex.practicum.mymarket.domain.OrderItemFull;
import ru.yandex.practicum.mymarket.dto.OrderDto;
import ru.yandex.practicum.mymarket.dto.OrderItemDto;
import ru.yandex.practicum.mymarket.service.SumService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Repository
public class OrderDatabaseClientRepository {

    private final SumService sumService;
    private final DatabaseClient databaseClient;

    public OrderDatabaseClientRepository(SumService sumService, DatabaseClient databaseClient) {
        this.sumService = sumService;
        this.databaseClient = databaseClient;
    }

    public Flux<OrderDto> findAll(Long userId) {
        return databaseClient.sql("""
                        select i.id, i.title, i.price, oi.order_id, oi.item_count as item_count, o.user_id
                        from orders o
                        left join order_items oi on o.id = oi.order_id
                        left join items i on oi.item_id = i.id
                        where o.user_id = :user_id
                        """)
                .bind("user_id", userId)
                .map((row, metadata) -> {
                    OrderItemFull itemFull = new OrderItemFull()
                            .setItemId(row.get("id", Long.class))
                            .setTitle(row.get("title", String.class))
                            .setPrice(row.get("price", Long.class))
                            .setOrderId(row.get("order_id", Long.class))
                            .setItemCount(row.get("item_count", Integer.class));

                    return itemFull;
                })
                .all().switchIfEmpty(Flux.empty())
                .groupBy(OrderItemFull::getOrderId)
                .flatMap(group -> group.collectList().map(this::createOrderDto));
    }

    private OrderDto createOrderDto(List<OrderItemFull> list) {
        List<OrderItemDto> orderItemDtoList = list.stream()
                .map(orderItemFull ->
                        new OrderItemDto(orderItemFull.getItemId(), orderItemFull.getTitle(), orderItemFull.getPrice(), orderItemFull.getItemCount()))
                .toList();
        return new OrderDto(list.getFirst().getOrderId(), orderItemDtoList, sumService.calculateSum(orderItemDtoList));
    }


    public Mono<OrderDto> findById(Long userId, Long orderId) {
        return databaseClient.sql("""
                        select i.id, i.title, i.price, oi.order_id, oi.item_count as item_count, o.user_id
                        from orders o
                        left join order_items oi on o.id = oi.order_id
                        left join items i on oi.item_id = i.id
                        where o.user_id = :user_id and o.id = :order_id
                        """)
                .bind("user_id", userId)
                .bind("order_id", orderId)
                .map((row, metadata) -> {
                    OrderItemFull itemFull = new OrderItemFull()
                            .setItemId(row.get("id", Long.class))
                            .setTitle(row.get("title", String.class))
                            .setPrice(row.get("price", Long.class))
                            .setOrderId(row.get("order_id", Long.class))
                            .setItemCount(row.get("item_count", Integer.class));

                    return itemFull;
                })
                .all()
                .collectList()
                .map((this::createOrderDto))
                    .switchIfEmpty(Mono.empty());
        }

}
