package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.comments.dto.CommentDto;
import ru.practicum.shareit.comments.dto.CommentGet;
import ru.practicum.shareit.comments.dto.CommentMapper;
import ru.practicum.shareit.comments.model.Comment;
import ru.practicum.shareit.comments.repository.CommentRepository;
import ru.practicum.shareit.error.exception.BadRequestException;
import ru.practicum.shareit.error.exception.CommentTextIsEmpty;
import ru.practicum.shareit.error.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoGet;
import ru.practicum.shareit.item.dto.ItemDtoGetBooking;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.utility.Status;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemServiceTest {
    @InjectMocks
    private ItemService service;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private BookingRepository bookingRepository;

    Item item = Item.builder()
            .id(0)
            .name("Имя")
            .description("Описание")
            .available(true)
            .owner(1)
            .request(1)
            .build();
    ItemDto dto = ItemDto.builder()
            .id(item.getId())
            .name(item.getName())
            .description(item.getDescription())
            .available(item.isAvailable())
            .owner(item.getOwner())
            .requestId(item.getRequest())
            .build();
    User user = User.builder()
            .id(1)
            .name("Имя")
            .email("почта@gmail.com")
            .build();

    @Test
    void createItemReturnException() {
        assertThrows(NotFoundException.class, () -> service.getItem(0, 20));
    }

    @Test
    void createItem() {
        Mockito.when(userRepository.findById(Mockito.anyInt())).thenReturn(Optional.ofNullable(user));
        Mockito.when(itemRepository.findByNameAndOwner("Имя", 1)).thenReturn(item);
        assertEquals(service.createItem(1, dto), ItemMapper.toItemDtoGet(item));
    }

    @Test
    void updateItemReturnNotFoundException() {
        Mockito.when(itemRepository.findById(0)).thenReturn(Optional.ofNullable(item));
        assertThrows(NotFoundException.class, () -> service.updateItem(0, 20, dto));
    }

    @Test
    void updateItem() {
        Item itemUpdate = Item.builder()
                .id(0)
                .name("Обновление")
                .description("Обновление")
                .available(true)
                .owner(1)
                .request(1)
                .build();
        ItemDto itemUpdateDto = ItemDto.builder()
                .id(itemUpdate.getId())
                .name(itemUpdate.getName())
                .description(itemUpdate.getDescription())
                .available(itemUpdate.isAvailable())
                .owner(itemUpdate.getOwner())
                .requestId(itemUpdate.getRequest())
                .build();
        ItemDtoGet itemUpdateDtoGet = ItemDtoGet.builder()
                .id(itemUpdate.getId())
                .name(itemUpdate.getName())
                .description(itemUpdate.getDescription())
                .available(itemUpdate.isAvailable())
                .requestId(itemUpdate.getRequest())
                .build();


        Mockito.when(itemRepository.findById(0)).thenReturn(Optional.ofNullable(item));
        Mockito.when(itemRepository.save(itemUpdate)).thenReturn(itemUpdate);
        assertEquals(service.updateItem(0, 1, itemUpdateDto), itemUpdateDtoGet);
    }

    @Test
    void getItem() {
        Mockito.when(itemRepository.findById(Mockito.anyInt())).thenReturn(Optional.ofNullable(item));
        Mockito.when(commentRepository.findByIdAndUserName(Mockito.anyInt())).thenReturn(new ArrayList<>());
        ItemDtoGetBooking itemDtoGetBooking = ItemMapper.itemDtoGetBooking(item,
                null,
                null,
                new ArrayList<>());
        assertEquals(service.getItem(item.getId(), item.getOwner()), itemDtoGetBooking);
    }

    @Test
    void getItemReturnNotFound() {
        Mockito.when(itemRepository.findById(Mockito.anyInt())).thenThrow(NotFoundException.class);
        assertThrows(NotFoundException.class, () -> service.getItem(0, 20));
    }

    @Test
    void getAllUserItems() {
        ItemDtoGetBooking itemDtoGetBooking = ItemMapper.itemDtoGetBooking(item, null, null, List.of());

        Mockito
                .when(itemRepository.findByOwner(Mockito.anyInt(), Mockito.any(PageRequest.class)))
                .thenReturn(List.of(item));

        assertEquals(service.getAllUserItems(1, 0, 1), List.of(itemDtoGetBooking));
    }

    @Test
    void searchItem() {
        List<ItemDtoGet> itemDtoGetList = new ArrayList<>();
        List<Item> itemList = new ArrayList<>();
        itemList.add(item);
        itemDtoGetList.add(ItemMapper.toItemDtoGet(item));

        Mockito.when(itemRepository.search("Имя")).thenReturn(itemList);
        assertEquals(service.searchItem("Имя"), itemDtoGetList);
    }

    @Test
    void searchItemWithEmptyText() {
        assertEquals(service.searchItem(""), new ArrayList<>());
    }


    @Test
    void createCommentReturnBadRequest() {
        CommentDto commentDto = CommentDto.builder()
                .itemId(1)
                .text("Текст")
                .authorId(1)
                .created(LocalDateTime.of(2024, 11,11,11,11))
                .build();
        assertThrows(BadRequestException.class, () -> service.createComment(0, 0, commentDto));
    }

    @Test
    void createCommentReturnCommentTextIsEmpty() {
        CommentDto commentDto = CommentDto.builder()
                .itemId(1)
                .text("")
                .authorId(1)
                .created(LocalDateTime.of(2024, 11,11,11,11))
                .build();
        Booking booking = Booking.builder()
                .id(0)
                .itemId(1)
                .bookerId(1)
                .status(Status.APPROVED)
                .start(LocalDateTime.of(2024, 11,10,11,11))
                .end(LocalDateTime.of(2024, 11,11,11,11))
                .build();

        Mockito
                .when(bookingRepository.findByBookerIdAndItemIdAndStatusAndEndLessThan(Mockito.anyInt(),
                        Mockito.anyInt(), Mockito.any(Status.class), Mockito.any(LocalDateTime.class)))
                .thenReturn(List.of(booking));
        assertThrows(CommentTextIsEmpty.class, () -> service.createComment(1, 1, commentDto));
    }

    @Test
    void createComment() {
        CommentDto commentDto = CommentDto.builder()
                .itemId(1)
                .text("Имя")
                .authorId(1)
                .created(LocalDateTime.of(2024, 11,11,11,11))
                .build();
        Booking booking = Booking.builder()
                .id(0)
                .itemId(1)
                .bookerId(1)
                .status(Status.APPROVED)
                .start(LocalDateTime.of(2024, 11,10,11,11))
                .end(LocalDateTime.of(2024, 11,11,11,11))
                .build();
        Comment comment = CommentMapper.toComment(commentDto, 1, 1);
        CommentGet commentGet = CommentMapper.toCommentGet(comment, "Имя");


        Mockito
                .when(bookingRepository.findByBookerIdAndItemIdAndStatusAndEndLessThan(Mockito.anyInt(),
                        Mockito.anyInt(), Mockito.any(Status.class), Mockito.any(LocalDateTime.class)))
                .thenReturn(List.of(booking));
        Mockito.when(userRepository.findById(Mockito.anyInt())).thenReturn(Optional.ofNullable(user));
        Mockito
                .when(commentRepository.findByItemIdAndAuthorIdAndText(Mockito.anyInt(), Mockito.anyInt(),
                        Mockito.anyString()))
                .thenReturn(comment);
        assertEquals(service.createComment(1, 1, commentDto), commentGet);
    }
}