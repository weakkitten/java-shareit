package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import ru.practicum.shareit.booking.dto.BookerDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoGet;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.error.exception.BadRequestException;
import ru.practicum.shareit.error.exception.BookingReject;
import ru.practicum.shareit.error.exception.NotFoundException;
import ru.practicum.shareit.error.exception.TimeException;
import ru.practicum.shareit.item.dto.ItemDtoBooking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.utility.Status;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class BookingServiceTest {
    @InjectMocks
    private BookingService service;

    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private UserRepository userRepository;

    @Test
    void createBookingEndBeforeStart() {
        BookingDto dto = BookingDto.builder()
                .id(0)
                .itemId(1)
                .start(LocalDateTime.of(2024, 6, 11, 11, 11))
                .end(LocalDateTime.of(2024, 6, 10, 11, 11))
                .build();

        assertThrows(TimeException.class, () -> service.createBooking(dto, 1));
    }

    @Test
    void createBookingEndEqualStart() {
        BookingDto dto = BookingDto.builder()
                .id(0)
                .itemId(1)
                .start(LocalDateTime.of(2024, 6, 11, 11, 11))
                .end(LocalDateTime.of(2024, 6, 11, 11, 11))
                .build();

        assertThrows(TimeException.class, () -> service.createBooking(dto, 1));
    }

    @Test
    void createBookingReturnNotFound() {
        BookingDto dto = BookingDto.builder()
                .id(0)
                .itemId(1)
                .start(LocalDateTime.of(2024, 6, 11, 11, 11))
                .end(LocalDateTime.of(2024, 6, 12, 11, 11))
                .build();

        Mockito.when(itemRepository.findById(Mockito.anyInt())).thenThrow(NotFoundException.class);
        assertThrows(NotFoundException.class, () -> service.createBooking(dto, 1));
    }

    @Test
    void createBookingReturnBadRequest() {
        BookingDto dto = BookingDto.builder()
                .id(0)
                .itemId(1)
                .start(LocalDateTime.of(2024, 6, 11, 11, 11))
                .end(LocalDateTime.of(2024, 6, 12, 11, 11))
                .build();
        Item item = Item.builder()
                .id(0)
                .name("Имя предмета")
                .owner(1)
                .request(1)
                .description("Описание предмета")
                .available(false)
                .build();

        Mockito.when(itemRepository.findById(Mockito.anyInt())).thenReturn(Optional.ofNullable(item));
        assertThrows(BadRequestException.class, () -> service.createBooking(dto, 1));
    }

    @Test
    void createBookingUserNotFound() {
        BookingDto dto = BookingDto.builder()
                .id(0)
                .itemId(1)
                .start(LocalDateTime.of(2024, 6, 11, 11, 11))
                .end(LocalDateTime.of(2024, 6, 12, 11, 11))
                .build();
        Item item = Item.builder()
                .id(0)
                .name("Имя предмета")
                .owner(1)
                .request(1)
                .description("Описание предмета")
                .available(true)
                .build();
        User user = User.builder()
                .id(0)
                .name("Имя пользователя")
                .email("Почта@gmail.com")
                .build();

        Mockito.when(itemRepository.findById(Mockito.anyInt())).thenReturn(Optional.ofNullable(item));
        Mockito.when(userRepository.findById(Mockito.anyInt())).thenThrow(NotFoundException.class);
        assertThrows(NotFoundException.class, () -> service.createBooking(dto, 1));
    }

    @Test
    void createBookingBookingReject() {
        BookingDto dto = BookingDto.builder()
                .id(0)
                .itemId(1)
                .start(LocalDateTime.of(2024, 6, 11, 11, 11))
                .end(LocalDateTime.of(2024, 6, 12, 11, 11))
                .build();
        Item item = Item.builder()
                .id(0)
                .name("Имя предмета")
                .owner(1)
                .request(1)
                .description("Описание предмета")
                .available(true)
                .build();
        User user = User.builder()
                .id(1)
                .name("Имя пользователя")
                .email("Почта@gmail.com")
                .build();

        Mockito.when(itemRepository.findById(Mockito.anyInt())).thenReturn(Optional.ofNullable(item));
        Mockito.when(userRepository.findById(Mockito.anyInt())).thenReturn(Optional.ofNullable(user));
        assertThrows(BookingReject.class, () -> service.createBooking(dto, 1));
    }

    @Test
    void createBooking() {
        BookingDto dto = BookingDto.builder()
                .id(0)
                .itemId(1)
                .start(LocalDateTime.of(2024, 6, 11, 11, 11))
                .end(LocalDateTime.of(2024, 6, 12, 11, 11))
                .build();
        Item item = Item.builder()
                .id(0)
                .name("Имя предмета")
                .owner(0)
                .request(1)
                .description("Описание предмета")
                .available(true)
                .build();
        User user = User.builder()
                .id(0)
                .name("Имя пользователя")
                .email("Почта@gmail.com")
                .build();
        Booking booking = BookingMapper.bookingDtoToBooking(dto, 0, Status.WAITING);
        BookerDto bookerDto = BookerDto.builder()
                .id(1)
                .build();
        ItemDtoBooking itemDtoBooking = ItemDtoBooking.builder()
                .id(1)
                .name("Имя предмета")
                .build();
        BookingDtoGet bookingDtoGet = BookingMapper.bookingToBookingDtoGet(booking, bookerDto, itemDtoBooking);


        Mockito.when(itemRepository.findById(Mockito.anyInt())).thenReturn(Optional.ofNullable(item));
        Mockito.when(userRepository.findById(Mockito.anyInt())).thenReturn(Optional.ofNullable(user));
        Mockito.when(bookingRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(booking));
        assertEquals(service.createBooking(dto, 1), bookingDtoGet);
    }

    @Test
    void updateBooking() {
    }

    @Test
    void getBooking() {
    }

    @Test
    void getAllUserBooking() {
    }

    @Test
    void getAllOwnerBooking() {
    }
}