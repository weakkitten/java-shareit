package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.comments.repository.CommentRepository;
import ru.practicum.shareit.error.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserService;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

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

    @Test
    void createItem() {
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

        Mockito.when(userRepository.findById(Mockito.anyInt())).thenReturn(Optional.ofNullable(user));
        Mockito.when(itemRepository.findByNameAndOwner("Имя", 1)).thenReturn(item);
        assertEquals(service.createItem(1, dto), ItemMapper.toItemDtoGet(item));
    }

    @Test
    void createItemReturnException() {
        Mockito.when(userRepository.findById(Mockito.anyInt())).thenThrow(NotFoundException.class);
    }

    @Test
    void updateItem() {
    }

    @Test
    void getItem() {
    }

    @Test
    void getAllUserItems() {
    }

    @Test
    void searchItem() {
    }

    @Test
    void createComment() {
    }
}