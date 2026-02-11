package ru.yandex.practicum.mymarket.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.r2dbc.core.DatabaseClient;
import reactor.test.StepVerifier;
import ru.yandex.practicum.mymarket.service.SumService;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataR2dbcTest
@Import({OrderDatabaseClientRepository.class, ItemDatabaseClientRepository.class, SumService.class})
class OrderDataClientRepositoryTest {

    @Autowired
    private SumService sumService;
    @Autowired
    private DatabaseClient databaseClient;

    @Autowired
    private ItemDatabaseClientRepository itemDatabaseClientRepository;
    @Autowired
    private OrderDatabaseClientRepository orderDatabaseClientRepository;

    @BeforeEach
    void setUp() {
        databaseClient.sql("delete from items").fetch().rowsUpdated().block();
        databaseClient.sql("delete from orders").fetch().rowsUpdated().block();
        databaseClient.sql("delete from order_items").fetch().rowsUpdated().block();

        databaseClient.sql("""
                        INSERT INTO items (id, title, description, img_path, price) 
                        VALUES (1, 'Бейсболка черная', 'Очень модная бейсболка черного цвета', '2.jpg', 1500)
                        """)
                .fetch()
                .rowsUpdated()
                .block();

        databaseClient.sql("""
                        INSERT INTO items(id, title, description, img_path, price) 
                        VALUES(2, 'Бейсболка красная', 'Очень модная бейсболка красного цвета', '1.jpg', 1500)
                        """)
                .fetch()
                .rowsUpdated()
                .block();

        databaseClient.sql("""
                        INSERT INTO items(id, title, description, img_path, price) 
                        VALUES(3, 'Футбольный мяч', 'Классный мяч для игры в футбол', '4.jpg', 400)
                        """)
                .fetch()
                .rowsUpdated()
                .block();

        databaseClient.sql("""
                        INSERT INTO items(id, title, description, img_path, price) 
                        VALUES(4, 'Зонтик', 'Зонтик, который всегда пригодится', '3.jpg', 2000)
                        """)
                .fetch()
                .rowsUpdated()
                .block();

        databaseClient.sql("insert into orders (id, user_id) VALUES (1, 17)").fetch().rowsUpdated().block();
        databaseClient.sql("insert into orders (id, user_id) VALUES (2, 17)").fetch().rowsUpdated().block();

        databaseClient.sql("insert into order_items (id, item_id, item_count, order_id) values (1, 1, 2, 1)").fetch().rowsUpdated().block();
        databaseClient.sql("insert into order_items (id, item_id, item_count, order_id) values (2, 4, 1, 1)").fetch().rowsUpdated().block();

        databaseClient.sql("insert into order_items (id, item_id, item_count, order_id) values (3, 3, 2, 2)").fetch().rowsUpdated().block();
    }

    @Test
    void findAll() {
        Long userId = 17L;

        StepVerifier.create(orderDatabaseClientRepository.findAll(userId))
                .expectNextMatches(orderDto -> {
                    assertEquals(1L, orderDto.getId());
                    assertEquals(5000, orderDto.getTotalSum());
                    assertEquals(2, orderDto.getItems().size());

                    assertEquals(1, orderDto.getItems().getFirst().getId());
                    assertEquals("Бейсболка черная", orderDto.getItems().getFirst().getTitle());
                    assertEquals(1500, orderDto.getItems().getFirst().getPrice());
                    assertEquals(2, orderDto.getItems().getFirst().getCount());

                    assertEquals(4, orderDto.getItems().getLast().getId());
                    assertEquals("Зонтик", orderDto.getItems().getLast().getTitle());
                    assertEquals(2000, orderDto.getItems().getLast().getPrice());
                    assertEquals(1, orderDto.getItems().getLast().getCount());
                    return true;
                })
                .expectNextMatches(orderDto -> {
                    assertEquals(2L, orderDto.getId());
                    assertEquals(800, orderDto.getTotalSum());
                    assertEquals(1, orderDto.getItems().size());

                    assertEquals(3, orderDto.getItems().getFirst().getId());
                    assertEquals("Футбольный мяч", orderDto.getItems().getFirst().getTitle());
                    assertEquals(400, orderDto.getItems().getFirst().getPrice());
                    assertEquals(2, orderDto.getItems().getFirst().getCount());
                    return true;
                })
                .expectNextCount(0)
                .verifyComplete();
    }

    @Test
    void findById() {
        Long userId = 17L;
        Long orderId = 2L;

        StepVerifier.create(orderDatabaseClientRepository.findById(userId, orderId))
                .expectNextMatches(orderDto -> {
                    assertEquals(2L, orderDto.getId());
                    assertEquals(800, orderDto.getTotalSum());
                    assertEquals(1, orderDto.getItems().size());

                    assertEquals(3, orderDto.getItems().getFirst().getId());
                    assertEquals("Футбольный мяч", orderDto.getItems().getFirst().getTitle());
                    assertEquals(400, orderDto.getItems().getFirst().getPrice());
                    assertEquals(2, orderDto.getItems().getFirst().getCount());
                    return true;
                })
                .expectNextCount(0)
                .verifyComplete();
    }
}