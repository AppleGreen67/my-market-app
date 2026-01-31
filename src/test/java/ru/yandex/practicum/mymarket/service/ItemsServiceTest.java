package ru.yandex.practicum.mymarket.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import ru.yandex.practicum.mymarket.dto.ItemDto;
import ru.yandex.practicum.mymarket.repository.ItemDatabaseClientRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.clearInvocations;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = ItemsService.class)
class ItemsServiceTest {

    @MockitoBean
    private CartService cartService;
    @MockitoBean
    private ItemDatabaseClientRepository itemRepository;

    @Autowired
    private ItemsService service;

    @BeforeEach
    void setUp() {
        clearInvocations(cartService);
        clearInvocations(itemRepository);
    }

    @Test
    public void findById() {
        Long itemId = 1L;

        ItemDto itemDto = new ItemDto();
        itemDto.setId(itemId);
        itemDto.setTitle("Бейсболка черная");
        itemDto.setDescription("Очень модная бейсболка черного цвета");
        itemDto.setImgPath("2.jpg");
        itemDto.setPrice(1500L);
        itemDto.setCount(3);
        when(itemRepository.findById(itemId)).thenReturn(Mono.just(itemDto));

        StepVerifier.create(service.find(itemId, 17L))
                .expectNextMatches(foundedItem ->{
                    assertEquals(1L, foundedItem.getId());
                    assertEquals("Бейсболка черная", foundedItem.getTitle());
                    assertEquals("Очень модная бейсболка черного цвета", foundedItem.getDescription());
                    assertEquals("2.jpg", foundedItem.getImgPath());
                    assertEquals(1500, foundedItem.getPrice());
                    assertEquals(3, foundedItem.getCount());
                    return true;
                })
                .verifyComplete();
    }


    @Test
    public void updateCountInCart() {
        Long itemId = 1L;
        String action = "PLUS";
        Long userId = 17L;

        ItemDto itemDto = new ItemDto();
        itemDto.setId(itemId);
        ItemDto itemDto1 = new ItemDto();
        itemDto1.setId(2L);
        when(cartService.updateCart(itemId, action, userId)).thenReturn(Flux.just(itemDto, itemDto1));

        StepVerifier.create(service.updateCountInCart(itemId, action, userId))
                .expectNextMatches(updatedItem ->{
                    assertEquals(1L, updatedItem.getId());
                    return true;
                })
                .verifyComplete();
    }

//    @Test
//    void getItems_noCart_searchAndPageAndAlpha() {
//        Long userId = 1L;
//
//        String search = "this";
//        String sort = "ALPHA";
//        Integer pageNumber = 1;
//        Integer pageSize = 3;
//
//        doAnswer(in -> {
//            PageRequest pageable = in.getArgument(0);
//            assertEquals(0, pageable.getPageNumber());
//            assertEquals(3, pageable.getPageSize());
//            assertEquals(Sort.by(Sort.Direction.ASC, "title"), pageable.getSort());
//
//            List<Item> items = Arrays.asList(
//                    getItem(2L, "title2 this", "description2", "imagePath", 0),
//                    getItem(3L, "title3", "description3 this", "imagePath", 0));
//
//            return new PageImpl(items);
//        }).when(itemRepository).findAllByTitleContainingOrDescriptionContaining(any(PageRequest.class), eq(search), eq(search));
//
//        when(cartRepository.findByUserId(userId)).thenReturn(Optional.empty());
//
//
//        List<List<ItemDto>> itemDtoList = service.getItems(userId, search, sort, pageNumber, pageSize);
//        assertEquals(1, itemDtoList.size());
//        assertEquals(2, itemDtoList.getFirst().size());
//
//        verify(itemRepository).findAllByTitleContainingOrDescriptionContaining(any(PageRequest.class), eq(search), eq(search));
//        verify(itemRepository, never()).findAll(any(PageRequest.class));
//        verify(cartRepository).findByUserId(userId);
//    }
//
//    @Test
//    void getItems_noCart_noSearchAndPageAndPrice() {
//        Long userId = 1L;
//
//        String search = "";
//        String sort = "PRICE";
//        Integer pageNumber = 1;
//        Integer pageSize = 3;
//
//        doAnswer(in -> {
//            PageRequest pageable = in.getArgument(0);
//            assertEquals(0, pageable.getPageNumber());
//            assertEquals(3, pageable.getPageSize());
//            assertEquals(Sort.by(Sort.Direction.ASC, "price"), pageable.getSort());
//
//            List<Item> items = Arrays.asList(
//                    getItem(2L, "title2 this", "description2", "imagePath", 0),
//                    getItem(3L, "title3", "description3 this", "imagePath", 0));
//
//            return new PageImpl(items);
//        }).when(itemRepository).findAll(any(PageRequest.class));
//
//        when(cartRepository.findByUserId(userId)).thenReturn(Optional.empty());
//
//
//        List<List<ItemDto>> itemDtoList = service.getItems(userId, search, sort, pageNumber, pageSize);
//        assertEquals(1, itemDtoList.size());
//        assertEquals(2, itemDtoList.getFirst().size());
//
//        verify(itemRepository).findAll(any(PageRequest.class));
//        verify(itemRepository, never()).findAllByTitleContainingOrDescriptionContaining(any(PageRequest.class), eq(search), eq(search));
//        verify(cartRepository).findByUserId(userId);
//    }
//
//    @Test
//    void getItems_noCart_searchEmpty() {
//        Long userId = 1L;
//
//        String search = "";
//        String sort = "NO";
//        Integer pageNumber = 1;
//        Integer pageSize = 3;
//
//        doAnswer(in -> {
//            PageRequest pageable = in.getArgument(0);
//            assertEquals(0, pageable.getPageNumber());
//            assertEquals(3, pageable.getPageSize());
//            assertEquals(Sort.unsorted(), pageable.getSort());
//
//            List<Item> items = Arrays.asList(getItem(1L, "title1", "description2", "imagePath", 0),
//                    getItem(2L, "title2", "description2", "imagePath", 0),
//                    getItem(3L, "title3", "description3", "imagePath", 0),
//                    getItem(4L, "title4", "description4", "imagePath", 0),
//                    getItem(5L, "title5", "description5", "imagePath", 0));
//
//            return new PageImpl(items);
//        }).when(itemRepository).findAll(any(PageRequest.class));
//        when(cartRepository.findByUserId(userId)).thenReturn(Optional.empty());
//
//
//        List<List<ItemDto>> itemDtoList = service.getItems(userId, search, sort, pageNumber, pageSize);
//        assertEquals(2, itemDtoList.size());
//        assertEquals(3, itemDtoList.get(0).size());
//        assertEquals(1, itemDtoList.get(0).get(0).getId());
//        assertEquals(0, itemDtoList.get(0).get(0).getCount());
//        assertEquals(2, itemDtoList.get(0).get(1).getId());
//        assertEquals(0, itemDtoList.get(0).get(1).getCount());
//        assertEquals(3, itemDtoList.get(0).get(2).getId());
//        assertEquals(0, itemDtoList.get(0).get(2).getCount());
//
//        assertEquals(2, itemDtoList.get(1).size());
//        assertEquals(4, itemDtoList.get(1).get(0).getId());
//        assertEquals(0, itemDtoList.get(1).get(0).getCount());
//        assertEquals(5, itemDtoList.get(1).get(1).getId());
//        assertEquals(0, itemDtoList.get(1).get(1).getCount());
//
//
//        verify(itemRepository).findAll(any(PageRequest.class));
//        verify(itemRepository, never()).findAllByTitleContainingOrDescriptionContaining(any(PageRequest.class), eq(search), eq(search));
//        verify(cartRepository).findByUserId(userId);
//    }
//
//    @Test
//    void getItems_itemInCart_searchEmpty() {
//        Long userId = 1L;
//
//        String search = "";
//        String sort = "NO";
//        Integer pageNumber = 1;
//        Integer pageSize = 3;
//
//        long id = 1L;
//        Item item = getItem(id, "title1", "description2", "imagePath", 0);
//        List<Item> items = Arrays.asList(item,
//                getItem(2L, "title2", "description2", "imagePath", 0),
//                getItem(3L, "title2", "description3", "imagePath", 0),
//                getItem(4L, "title2", "description4", "imagePath", 0),
//                getItem(5L, "title2", "description5", "imagePath", 0));
//
//        when(itemRepository.findAll(any(PageRequest.class))).thenReturn(new PageImpl(items));
//
//        CartItem cartItem = new CartItem();
//        cartItem.setId(1L);
//        cartItem.setItem(item);
//        cartItem.setCount(23);
//
//        Cart cart = new Cart();
//        cart.getItems().add(cartItem);
//
//        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));
//        when(cartService.findCartItemByItem(cart, id)).thenReturn(Optional.of(cartItem));
//
//
//        List<List<ItemDto>> itemDtoList = service.getItems(userId, search, sort, pageNumber, pageSize);
//        assertEquals(2, itemDtoList.size());
//        assertEquals(3, itemDtoList.get(0).size());
//        assertEquals(1, itemDtoList.get(0).get(0).getId());
//        assertEquals(23, itemDtoList.get(0).get(0).getCount());
//        assertEquals(2, itemDtoList.get(0).get(1).getId());
//        assertEquals(0, itemDtoList.get(0).get(1).getCount());
//        assertEquals(3, itemDtoList.get(0).get(2).getId());
//        assertEquals(0, itemDtoList.get(0).get(2).getCount());
//
//        assertEquals(2, itemDtoList.get(1).size());
//        assertEquals(4, itemDtoList.get(1).get(0).getId());
//        assertEquals(0, itemDtoList.get(1).get(0).getCount());
//        assertEquals(5, itemDtoList.get(1).get(1).getId());
//        assertEquals(0, itemDtoList.get(1).get(1).getCount());
//
//
//        verify(itemRepository).findAll(any(PageRequest.class));
//        verify(itemRepository, never()).findAllByTitleContainingOrDescriptionContaining(any(PageRequest.class), eq(search), eq(search));
//        verify(cartRepository).findByUserId(userId);
//    }
//
//    @Test
//    void updateCountInCart() {
//        Long userId = 1L;
//
//        Long id = 1L;
//        String action = "PLUS";
//
//        List<ItemDto> items = Arrays.asList(new ItemDto(1L, "title1", "description1", "imageUrl", 11L, 0),
//                new ItemDto(2L, "title2", "description2", "imageUrl", 22L, 0),
//                new ItemDto(3L, "title3", "description3", "imageUrl", 33L, 0));
//
//        when(cartService.updateCart(id, action, userId)).thenReturn(items);
//
//        ItemDto itemDto = service.updateCountInCart(id, action, userId);
//        assertNotNull(itemDto);
//        assertEquals(id, itemDto.getId());
//        assertEquals("title1", itemDto.getTitle());
//        assertEquals(0, itemDto.getCount());
//
//        verify(cartService).updateCart(id, action, userId);
//    }
//
//    @Test
//    void updateCountInCart_exception() {
//        Long userId = 1L;
//
//        Long id = 1L;
//        String action = "PLUS";
//
//        List<ItemDto> items = Arrays.asList(new ItemDto(2L, "title2", "description2", "imageUrl", 22L, 0),
//                new ItemDto(3L, "title3", "description3", "imageUrl", 33L, 0));
//
//        when(cartService.updateCart(id, action, userId)).thenReturn(items);
//
//        try {
//            service.updateCountInCart(id, action, userId);
//            fail();
//        } catch (Exception e) {
//            assertInstanceOf(NoSuchElementException.class, e);
//        }
//
//        verify(cartService).updateCart(id, action, userId);
//    }

//    @Test
//    void find_exception() {
//        Long userId = 1L;
//
//        Long id = 1L;
//
//        when(itemRepository.findById(id)).thenReturn(Optional.empty());
//
//        try {
//            service.find(id, userId);
//            fail();
//        } catch (Exception e) {
//            assertInstanceOf(NoSuchElementException.class, e);
//        }
//    }
//
//    @Test
//    void find_noCart() {
//        Long userId = 1L;
//
//        Long id = 2L;
//
//        Item item = getItem(2L, "title2", "description2", "imagePath", 22L);
//
//        when(itemRepository.findById(id)).thenReturn(Optional.of(item));
//
//        when(cartRepository.findByUserId(userId)).thenReturn(Optional.empty());
//
//        ItemDto itemDto = service.find(id, userId);
//        assertNotNull(itemDto);
//        assertEquals(id, itemDto.getId());
//        assertEquals("title2", itemDto.getTitle());
//        assertEquals(0, itemDto.getCount());
//
//        verify(itemRepository).findById(id);
//        verify(cartRepository).findByUserId(userId);
//    }

//    @Test
    void find_noItemInCart() {
//        Long userId = 1L;
//
//        Long id = 2L;
//
//        Item item = getItem(id, "title2", "description2", "imagePath", 22L);
//        when(itemRepository.findById(id)).thenReturn(Mono.just(item));
//
//        CartItem cartItem = new CartItem();
//        cartItem.setId(1L);
////        cartItem.setCartId(1L);
//        cartItem.setItemId(id);
////        cartItem.setItemTitle("title2");
//        cartItem.setCount(23);
//
//        Cart cart = new Cart();
//        cart.setId(1L);
//
//        when(cartRepository.findByUserId(userId)).thenReturn(Mono.just(cart));
////        when(cartItemRepository.findByCartIdAndItemId(cart.getId(), id)).thenReturn(Mono.just(cartItem));
//
//        StepVerifier.create(service.find(id, userId))
//                .expectNextMatches(foundedItem ->{
//                    assertEquals(id, foundedItem.getId());
//                    assertEquals("title2", foundedItem.getTitle());
//                    assertEquals(23, foundedItem.getCount());
//                    return true;
//                })
//                .expectNextCount(0);

//        ItemDto itemDto = service.find(id, userId);
//        assertNotNull(itemDto);
//        assertEquals(id, itemDto.getId());
//        assertEquals("title2", itemDto.getTitle());
//        assertEquals(0, itemDto.getCount());

//        verify(itemRepository).findById(id);
//        verify(cartRepository).findByUserId(userId);
    }

//    @Test
//    void find_countFromItemInCart() {
//        Long userId = 1L;
//
//        Long id = 2L;
//
//        Item item = getItem(2L, "title2", "description2", "imagePath", 22L);
//
//        when(itemRepository.findById(id)).thenReturn(Optional.of(item));
//
//        CartItem cartItem = new CartItem();
//        cartItem.setId(2L);
//        cartItem.setItem(item);
//        cartItem.setCount(23);
//
//        Cart cart = new Cart();
//        cart.getItems().add(cartItem);
//
//        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));
//        when(cartService.findCartItemByItem(cart, id)).thenReturn(Optional.of(cartItem));
//
//        ItemDto itemDto = service.find(id, userId);
//        assertNotNull(itemDto);
//        assertEquals(id, itemDto.getId());
//        assertEquals("title2", itemDto.getTitle());
//        assertEquals(23, itemDto.getCount());
//
//        verify(itemRepository).findById(id);
//        verify(cartRepository).findByUserId(userId);
//    }

}