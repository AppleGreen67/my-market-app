package ru.yandex.practicum.mymarket.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import ru.yandex.practicum.mymarket.domain.Item;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.yandex.practicum.mymarket.utils.ItemsUtils.getItem;

@DataJpaTest
class ItemRepositoryTest {

    @Autowired
    private ItemRepository repository;

    @BeforeEach
    void setUp() {
       repository.deleteAll();
    }

    @Test
    void findAllByTitleContainingOrDescriptionContaining() {
        Item item1 = getItem("title1", "description2", "imagePath", 1);
        repository.save(item1);

        Item item2 = getItem("title2 this", "description2", "imagePath", 2);
        repository.save(item2);

        Item item3 = getItem("title3", "description3", "imagePath", 3);
        repository.save(item3);

        Item item4 = getItem("title4", "description4 this", "imagePath", 4);
        repository.save(item4);

        Item item5 = getItem("title5", "description5", "imagePath", 5);
        repository.save(item5);

        Item item6 = getItem("title6", "description6", "imagePath", 6);
        repository.save(item6);

        List<Item> allSavedItems = repository.findAll();
        assertEquals(6, allSavedItems.size());


        PageRequest pageable = PageRequest.of(0, 10);

        Page<Item> itemPage = repository.findAllByTitleContainingOrDescriptionContaining(pageable, "this", "this");
        List<Item> thisItems = itemPage.get().toList();
        assertEquals(2, thisItems.size());

        assertEquals("title2 this", thisItems.get(0).getTitle());
        assertEquals("description4 this", thisItems.get(1).getDescription());
    }

    @Test
    void findAllByPageable() {
        Item item1 = getItem("title1", "description2", "imagePath", 6);
        repository.save(item1);

        Item item2 = getItem("title2", "description2", "imagePath", 5);
        repository.save(item2);

        Item item3 = getItem("title3", "description3", "imagePath", 4);
        repository.save(item3);

        Item item4 = getItem("title4", "description4is", "imagePath", 3);
        repository.save(item4);

        Item item5 = getItem("title5", "description5", "imagePath", 2);
        repository.save(item5);

        Item item6 = getItem("title6", "description6", "imagePath", 1);
        repository.save(item6);

        List<Item> allSavedItems = repository.findAll();
        assertEquals(6, allSavedItems.size());
        assertEquals("title1", allSavedItems.get(0).getTitle());
        assertEquals("title2", allSavedItems.get(1).getTitle());
        assertEquals("title3", allSavedItems.get(2).getTitle());
        assertEquals("title4", allSavedItems.get(3).getTitle());
        assertEquals("title5", allSavedItems.get(4).getTitle());
        assertEquals("title6", allSavedItems.get(5).getTitle());

        PageRequest pageable = PageRequest.of(1, 2, Sort.Direction.ASC, "price");;

        Page<Item> itemPage = repository.findAll(pageable);
        List<Item> thisItems = itemPage.get().toList();
        assertEquals(2, thisItems.size());

        assertEquals("title4", thisItems.get(0).getTitle());
        assertEquals("title3", thisItems.get(1).getTitle());
    }
}