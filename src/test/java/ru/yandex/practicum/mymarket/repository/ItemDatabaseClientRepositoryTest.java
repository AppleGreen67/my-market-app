package ru.yandex.practicum.mymarket.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.r2dbc.core.DatabaseClient;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataR2dbcTest
@Import(ItemDatabaseClientRepository.class)
class ItemDatabaseClientRepositoryTest {

    @Autowired
    private DatabaseClient databaseClient;

    @Autowired
    private ItemDatabaseClientRepository repository;

    @BeforeEach
    void setUp() {
        databaseClient.sql("delete from items").fetch().rowsUpdated().block();
        databaseClient.sql("delete from carts").fetch().rowsUpdated().block();
        databaseClient.sql("delete from cart_items").fetch().rowsUpdated().block();

        databaseClient.sql("""
                        INSERT INTO items (title, description, img_path, price)
                                VALUES ('Бейсболка черная', 'Очень модная бейсболка черного цвета', '2.jpg', 1500)
                        """)
                .fetch()
                .rowsUpdated()
                .block();

        databaseClient.sql("""
                        INSERT INTO items(title, description, img_path, price)
                        VALUES('Бейсболка красная', 'Очень модная бейсболка красного цвета', '1.jpg', 1500)
                        """)
                .fetch()
                .rowsUpdated()
                .block();

        databaseClient.sql("""
                        INSERT INTO items(title, description, img_path, price)
                        VALUES('Футбольный мяч', 'Классный мяч для игры в футбол', '4.jpg', 400)
                        """)
                .fetch()
                .rowsUpdated()
                .block();

        databaseClient.sql("""
                        INSERT INTO items(title, description, img_path, price)
                        VALUES('Зонтик', 'Зонтик, который всегда пригодится', '3.jpg', 2000)
                        """)
                .fetch()
                .rowsUpdated()
                .block();
    }

    @Test
    public void findByIdTest() {
        StepVerifier.create(repository.findAll())
                .expectNextMatches(itemDto->  {
                    assertEquals(1L, itemDto.getId());
                    return true;
                })
                .expectNextMatches(itemDto->  {
                    assertEquals(2L, itemDto.getId());
                    return true;
                })
                .expectNextMatches(itemDto->  {
                    assertEquals(3L, itemDto.getId());
                    return true;
                })
                .expectNextMatches(itemDto->  {
                    assertEquals(4L, itemDto.getId());
                    return true;
                })
                .expectNextCount(0)
                .verifyComplete();

        StepVerifier.create(repository.findById(1L))
                .expectNextMatches(itemDto -> {
                    assertEquals(1L, itemDto.getId());
                    assertEquals("Бейсболка черная", itemDto.getTitle());
                    assertEquals("Очень модная бейсболка черного цвета", itemDto.getDescription());
                    assertEquals("2.jpg", itemDto.getImgPath());
                    assertEquals(1500, itemDto.getPrice());
                    assertEquals(0, itemDto.getCount());
                    return true;
                })
                .verifyComplete();

        databaseClient.sql("""
                        insert into cart_items (cart_id, item_id, item_count) values (1, 1, 23);
                        """)
                .fetch()
                .rowsUpdated()
                .block();

        StepVerifier.create(repository.findById(1L))
                .expectNextMatches(itemDto -> {
                    assertEquals(1L, itemDto.getId());
                    assertEquals("Бейсболка черная", itemDto.getTitle());
                    assertEquals("Очень модная бейсболка черного цвета", itemDto.getDescription());
                    assertEquals("2.jpg", itemDto.getImgPath());
                    assertEquals(1500, itemDto.getPrice());
                    assertEquals(23, itemDto.getCount());
                    return true;
                })
                .verifyComplete();
    }
}