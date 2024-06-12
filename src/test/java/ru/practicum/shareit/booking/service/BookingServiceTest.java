package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.booking.dto.BookerDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoGet;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.error.exception.*;
import ru.practicum.shareit.item.dto.ItemDtoBooking;
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
    void updateBookingBookingReject() {
        Booking booking = Booking.builder()
                .id(0)
                .itemId(1)
                .start(LocalDateTime.of(2024, 6, 11, 11, 11))
                .end(LocalDateTime.of(2024, 6, 12, 11, 11))
                .status(Status.WAITING)
                .bookerId(1)
                .build();
        Item item = Item.builder()
                .id(0)
                .name("Имя предмета")
                .owner(0)
                .request(1)
                .description("Описание предмета")
                .available(true)
                .build();

        Mockito.when(bookingRepository.findById(Mockito.anyInt())).thenReturn(Optional.ofNullable(booking));
        Mockito.when(itemRepository.findById(Mockito.anyInt())).thenReturn(Optional.ofNullable(item));
        assertThrows(BookingReject.class, () -> service.updateBooking(12, 1, true));
    }

    @Test
    void updateBookingBadRequestException() {
        Booking booking = Booking.builder()
                .id(0)
                .itemId(1)
                .start(LocalDateTime.of(2024, 6, 11, 11, 11))
                .end(LocalDateTime.of(2024, 6, 12, 11, 11))
                .status(Status.APPROVED)
                .bookerId(1)
                .build();
        Item item = Item.builder()
                .id(0)
                .name("Имя предмета")
                .owner(0)
                .request(1)
                .description("Описание предмета")
                .available(true)
                .build();

        Mockito.when(bookingRepository.findById(Mockito.anyInt())).thenReturn(Optional.ofNullable(booking));
        Mockito.when(itemRepository.findById(Mockito.anyInt())).thenReturn(Optional.ofNullable(item));
        assertThrows(BadRequestException.class, () -> service.updateBooking(0, 1, true));
    }

    @Test
    void updateBookingAccept() {
        Booking booking = Booking.builder()
                .id(0)
                .itemId(1)
                .start(LocalDateTime.of(2024, 6, 11, 11, 11))
                .end(LocalDateTime.of(2024, 6, 12, 11, 11))
                .status(Status.WAITING)
                .bookerId(1)
                .build();
        Item item = Item.builder()
                .id(0)
                .name("Имя предмета")
                .owner(0)
                .request(1)
                .description("Описание предмета")
                .available(true)
                .build();
        BookerDto bookerDto = BookerDto.builder()
                .id(1)
                .build();
        ItemDtoBooking itemDtoBooking = ItemDtoBooking.builder()
                .id(1)
                .name("Имя предмета")
                .build();
        Booking bookingAccept = Booking.builder()
                .id(0)
                .itemId(1)
                .start(LocalDateTime.of(2024, 6, 11, 11, 11))
                .end(LocalDateTime.of(2024, 6, 12, 11, 11))
                .status(Status.APPROVED)
                .bookerId(1)
                .build();
        BookingDtoGet bookingDtoGet = BookingMapper.bookingToBookingDtoGet(bookingAccept, bookerDto, itemDtoBooking);

        Mockito.when(bookingRepository.findById(Mockito.anyInt())).thenReturn(Optional.ofNullable(booking));
        Mockito.when(itemRepository.findById(Mockito.anyInt())).thenReturn(Optional.ofNullable(item));
        Mockito.when(bookingRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(booking));
        assertEquals(service.updateBooking(0, 1, true), bookingDtoGet);
    }

    @Test
    void updateBookingReject() {
        Booking booking = Booking.builder()
                .id(0)
                .itemId(1)
                .start(LocalDateTime.of(2024, 6, 11, 11, 11))
                .end(LocalDateTime.of(2024, 6, 12, 11, 11))
                .status(Status.WAITING)
                .bookerId(1)
                .build();
        Item item = Item.builder()
                .id(0)
                .name("Имя предмета")
                .owner(0)
                .request(1)
                .description("Описание предмета")
                .available(true)
                .build();
        BookerDto bookerDto = BookerDto.builder()
                .id(1)
                .build();
        ItemDtoBooking itemDtoBooking = ItemDtoBooking.builder()
                .id(1)
                .name("Имя предмета")
                .build();
        Booking bookingAccept = Booking.builder()
                .id(0)
                .itemId(1)
                .start(LocalDateTime.of(2024, 6, 11, 11, 11))
                .end(LocalDateTime.of(2024, 6, 12, 11, 11))
                .status(Status.REJECTED)
                .bookerId(1)
                .build();
        BookingDtoGet bookingDtoGet = BookingMapper.bookingToBookingDtoGet(bookingAccept, bookerDto, itemDtoBooking);

        Mockito.when(bookingRepository.findById(Mockito.anyInt())).thenReturn(Optional.ofNullable(booking));
        Mockito.when(itemRepository.findById(Mockito.anyInt())).thenReturn(Optional.ofNullable(item));
        Mockito.when(bookingRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(booking));
        assertEquals(service.updateBooking(0, 1, false), bookingDtoGet);
    }

    @Test
    void getBookingReturnNotFoundException() {
        assertThrows(NotFoundException.class, () -> service.getBooking(1, 1));
    }

    @Test
    void getBookingReturnBookingRejectWitchBookerId() {
        Booking booking = Booking.builder()
                .id(0)
                .itemId(1)
                .start(LocalDateTime.of(2024, 6, 11, 11, 11))
                .end(LocalDateTime.of(2024, 6, 12, 11, 11))
                .status(Status.WAITING)
                .bookerId(0)
                .build();
        Item item = Item.builder()
                .id(0)
                .name("Имя предмета")
                .owner(0)
                .request(1)
                .description("Описание предмета")
                .available(true)
                .build();
        Mockito.when(bookingRepository.findById(Mockito.anyInt())).thenReturn(Optional.ofNullable(booking));
        Mockito.when(itemRepository.findById(Mockito.anyInt())).thenReturn(Optional.ofNullable(item));
        assertThrows(BookingReject.class, () -> service.getBooking(1,1));
    }

    @Test
    void getBookingWitchBookerId() {
        Booking booking = Booking.builder()
                .id(0)
                .itemId(1)
                .start(LocalDateTime.of(2024, 6, 11, 11, 11))
                .end(LocalDateTime.of(2024, 6, 12, 11, 11))
                .status(Status.WAITING)
                .bookerId(1)
                .build();
        Item item = Item.builder()
                .id(0)
                .name("Имя предмета")
                .owner(1)
                .request(1)
                .description("Описание предмета")
                .available(true)
                .build();
        BookerDto bookerDto = BookerDto.builder()
                .id(1)
                .build();
        ItemDtoBooking itemDtoBooking = ItemDtoBooking.builder()
                .id(1)
                .name("Имя предмета")
                .build();
        BookingDtoGet bookingDtoGet = BookingMapper.bookingToBookingDtoGet(booking, bookerDto, itemDtoBooking);

        Mockito.when(bookingRepository.findById(Mockito.anyInt())).thenReturn(Optional.ofNullable(booking));
        Mockito.when(itemRepository.findById(Mockito.anyInt())).thenReturn(Optional.ofNullable(item));
        Mockito.when(bookingRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(booking));
        assertEquals(service.getBooking(1,1), bookingDtoGet);
    }

    @Test
    void getBookingWitchUserId() {
        Booking booking = Booking.builder()
                .id(0)
                .itemId(1)
                .start(LocalDateTime.of(2024, 6, 11, 11, 11))
                .end(LocalDateTime.of(2024, 6, 12, 11, 11))
                .status(Status.WAITING)
                .bookerId(0)
                .build();
        Item item = Item.builder()
                .id(0)
                .name("Имя предмета")
                .owner(1)
                .request(1)
                .description("Описание предмета")
                .available(true)
                .build();
        BookerDto bookerDto = BookerDto.builder()
                .id(0)
                .build();
        ItemDtoBooking itemDtoBooking = ItemDtoBooking.builder()
                .id(1)
                .name("Имя предмета")
                .build();
        BookingDtoGet bookingDtoGet = BookingMapper.bookingToBookingDtoGet(booking, bookerDto, itemDtoBooking);

        Mockito.when(bookingRepository.findById(Mockito.anyInt())).thenReturn(Optional.ofNullable(booking));
        Mockito.when(itemRepository.findById(Mockito.anyInt())).thenReturn(Optional.ofNullable(item));
        Mockito.when(bookingRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(booking));
        assertEquals(service.getBooking(1,1), bookingDtoGet);
    }

    @Test
    void getAllUserBookingReturnNotFound() {
        assertThrows(NotFoundException.class, () -> service.getAllUserBooking(1, "Стейт", 0, 1));
    }

    @Test
    void getAllUserBookingReturnUnsupportedState() {
        User user = User.builder()
                .id(0)
                .name("Имя пользователя")
                .email("Почта@gmail.com")
                .build();
        Mockito.when(userRepository.findById(Mockito.anyInt())).thenReturn(Optional.ofNullable(user));
        assertThrows(UnsupportedState.class, () -> service.getAllUserBooking(1, "Стейт", 0, 1));
    }

    @Test
    void getAllUserBookingStateALL() {
        List<Booking> bookingList = new ArrayList<>();
        User user = User.builder()
                .id(0)
                .name("Имя пользователя")
                .email("Почта@gmail.com")
                .build();
        Booking booking = Booking.builder()
                .id(0)
                .itemId(1)
                .start(LocalDateTime.of(2024, 6, 11, 11, 11))
                .end(LocalDateTime.of(2024, 6, 12, 11, 11))
                .status(Status.WAITING)
                .bookerId(0)
                .build();
        Item item = Item.builder()
                .id(0)
                .name("Имя предмета")
                .owner(1)
                .request(1)
                .description("Описание предмета")
                .available(true)
                .build();
        BookerDto bookerDto = BookerDto.builder()
                .id(0)
                .build();
        ItemDtoBooking itemDtoBooking = ItemDtoBooking.builder()
                .id(1)
                .name("Имя предмета")
                .build();
        BookingDtoGet bookingDtoGet = BookingMapper.bookingToBookingDtoGet(booking, bookerDto, itemDtoBooking);
        bookingList.add(booking);
        Mockito.when(userRepository.findById(Mockito.anyInt())).thenReturn(Optional.ofNullable(user));
        Mockito
                .when(bookingRepository.findByBookerIdOrderByStartDesc(Mockito.anyInt(), Mockito.any(PageRequest.class)))
                        .thenReturn(bookingList);
        Mockito.when(itemRepository.findById(Mockito.anyInt())).thenReturn(Optional.ofNullable(item));
        assertEquals(service.getAllUserBooking(1, "ALL", 0, 1), List.of(bookingDtoGet));
    }

    @Test
    void getAllUserBookingStateWaiting() {
        List<Booking> bookingList = new ArrayList<>();
        User user = User.builder()
                .id(0)
                .name("Имя пользователя")
                .email("Почта@gmail.com")
                .build();
        Booking booking = Booking.builder()
                .id(0)
                .itemId(1)
                .start(LocalDateTime.of(2024, 6, 11, 11, 11))
                .end(LocalDateTime.of(2024, 6, 12, 11, 11))
                .status(Status.WAITING)
                .bookerId(0)
                .build();
        Item item = Item.builder()
                .id(0)
                .name("Имя предмета")
                .owner(1)
                .request(1)
                .description("Описание предмета")
                .available(true)
                .build();
        BookerDto bookerDto = BookerDto.builder()
                .id(0)
                .build();
        ItemDtoBooking itemDtoBooking = ItemDtoBooking.builder()
                .id(1)
                .name("Имя предмета")
                .build();
        BookingDtoGet bookingDtoGet = BookingMapper.bookingToBookingDtoGet(booking, bookerDto, itemDtoBooking);
        bookingList.add(booking);
        Mockito.when(userRepository.findById(Mockito.anyInt())).thenReturn(Optional.ofNullable(user));
        Mockito
                .when(bookingRepository.findByBookerIdAndStatusOrderByStartDesc(Mockito.anyInt(),
                        Mockito.any(Status.class), Mockito.any(PageRequest.class)))
                .thenReturn(bookingList);
        Mockito.when(itemRepository.findById(Mockito.anyInt())).thenReturn(Optional.ofNullable(item));
        assertEquals(service.getAllUserBooking(1, "WAITING", 0, 1), List.of(bookingDtoGet));
    }

    @Test
    void getAllUserBookingStateRejected() {
        List<Booking> bookingList = new ArrayList<>();
        User user = User.builder()
                .id(0)
                .name("Имя пользователя")
                .email("Почта@gmail.com")
                .build();
        Booking booking = Booking.builder()
                .id(0)
                .itemId(1)
                .start(LocalDateTime.of(2024, 6, 11, 11, 11))
                .end(LocalDateTime.of(2024, 6, 12, 11, 11))
                .status(Status.WAITING)
                .bookerId(0)
                .build();
        Item item = Item.builder()
                .id(0)
                .name("Имя предмета")
                .owner(1)
                .request(1)
                .description("Описание предмета")
                .available(true)
                .build();
        BookerDto bookerDto = BookerDto.builder()
                .id(0)
                .build();
        ItemDtoBooking itemDtoBooking = ItemDtoBooking.builder()
                .id(1)
                .name("Имя предмета")
                .build();
        BookingDtoGet bookingDtoGet = BookingMapper.bookingToBookingDtoGet(booking, bookerDto, itemDtoBooking);
        bookingList.add(booking);
        Mockito.when(userRepository.findById(Mockito.anyInt())).thenReturn(Optional.ofNullable(user));
        Mockito
                .when(bookingRepository.findByBookerIdAndStatusOrderByStartDesc(Mockito.anyInt(),
                        Mockito.any(Status.class), Mockito.any(PageRequest.class)))
                .thenReturn(bookingList);
        Mockito.when(itemRepository.findById(Mockito.anyInt())).thenReturn(Optional.ofNullable(item));
        assertEquals(service.getAllUserBooking(1, "REJECTED", 0, 1), List.of(bookingDtoGet));
    }

    @Test
    void getAllUserBookingStateFuture() {
        List<Booking> bookingList = new ArrayList<>();
        User user = User.builder()
                .id(0)
                .name("Имя пользователя")
                .email("Почта@gmail.com")
                .build();
        Booking booking = Booking.builder()
                .id(0)
                .itemId(1)
                .start(LocalDateTime.of(2024, 6, 11, 11, 11))
                .end(LocalDateTime.of(2024, 6, 12, 11, 11))
                .status(Status.WAITING)
                .bookerId(0)
                .build();
        Item item = Item.builder()
                .id(0)
                .name("Имя предмета")
                .owner(1)
                .request(1)
                .description("Описание предмета")
                .available(true)
                .build();
        BookerDto bookerDto = BookerDto.builder()
                .id(0)
                .build();
        ItemDtoBooking itemDtoBooking = ItemDtoBooking.builder()
                .id(1)
                .name("Имя предмета")
                .build();
        BookingDtoGet bookingDtoGet = BookingMapper.bookingToBookingDtoGet(booking, bookerDto, itemDtoBooking);
        bookingList.add(booking);
        Mockito.when(userRepository.findById(Mockito.anyInt())).thenReturn(Optional.ofNullable(user));
        Mockito
                .when(bookingRepository.future(Mockito.anyInt(), Mockito.any(LocalDateTime.class), Mockito.any(Status.class),
                        Mockito.any(Status.class), Mockito.any(PageRequest.class)))
                .thenReturn(bookingList);
        Mockito.when(itemRepository.findById(Mockito.anyInt())).thenReturn(Optional.ofNullable(item));
        assertEquals(service.getAllUserBooking(1, "FUTURE", 0, 1), List.of(bookingDtoGet));
    }

    @Test
    void getAllUserBookingStatePaste() {
        List<Booking> bookingList = new ArrayList<>();
        User user = User.builder()
                .id(0)
                .name("Имя пользователя")
                .email("Почта@gmail.com")
                .build();
        Booking booking = Booking.builder()
                .id(0)
                .itemId(1)
                .start(LocalDateTime.of(2024, 6, 11, 11, 11))
                .end(LocalDateTime.of(2024, 6, 12, 11, 11))
                .status(Status.WAITING)
                .bookerId(0)
                .build();
        Item item = Item.builder()
                .id(0)
                .name("Имя предмета")
                .owner(1)
                .request(1)
                .description("Описание предмета")
                .available(true)
                .build();
        BookerDto bookerDto = BookerDto.builder()
                .id(0)
                .build();
        ItemDtoBooking itemDtoBooking = ItemDtoBooking.builder()
                .id(1)
                .name("Имя предмета")
                .build();
        BookingDtoGet bookingDtoGet = BookingMapper.bookingToBookingDtoGet(booking, bookerDto, itemDtoBooking);
        bookingList.add(booking);
        Mockito.when(userRepository.findById(Mockito.anyInt())).thenReturn(Optional.ofNullable(user));
        Mockito
                .when(bookingRepository.paste(Mockito.anyInt(), Mockito.any(LocalDateTime.class), Mockito.any(Status.class),
                        Mockito.any(PageRequest.class)))
                .thenReturn(bookingList);
        Mockito.when(itemRepository.findById(Mockito.anyInt())).thenReturn(Optional.ofNullable(item));
        assertEquals(service.getAllUserBooking(1, "PAST", 0, 1), List.of(bookingDtoGet));
    }

    @Test
    void getAllUserBookingStateCurrent() {
        List<Booking> bookingList = new ArrayList<>();
        User user = User.builder()
                .id(0)
                .name("Имя пользователя")
                .email("Почта@gmail.com")
                .build();
        Booking booking = Booking.builder()
                .id(0)
                .itemId(1)
                .start(LocalDateTime.of(2024, 6, 11, 11, 11))
                .end(LocalDateTime.of(2024, 6, 12, 11, 11))
                .status(Status.WAITING)
                .bookerId(0)
                .build();
        Item item = Item.builder()
                .id(0)
                .name("Имя предмета")
                .owner(1)
                .request(1)
                .description("Описание предмета")
                .available(true)
                .build();
        BookerDto bookerDto = BookerDto.builder()
                .id(0)
                .build();
        ItemDtoBooking itemDtoBooking = ItemDtoBooking.builder()
                .id(1)
                .name("Имя предмета")
                .build();
        BookingDtoGet bookingDtoGet = BookingMapper.bookingToBookingDtoGet(booking, bookerDto, itemDtoBooking);
        bookingList.add(booking);
        Mockito.when(userRepository.findById(Mockito.anyInt())).thenReturn(Optional.ofNullable(user));
        Mockito
                .when(bookingRepository.current(Mockito.anyInt(), Mockito.any(LocalDateTime.class),
                        Mockito.any(Status.class), Mockito.any(Status.class), Mockito.any(PageRequest.class)))
                .thenReturn(bookingList);
        Mockito.when(itemRepository.findById(Mockito.anyInt())).thenReturn(Optional.ofNullable(item));
        assertEquals(service.getAllUserBooking(1, "CURRENT", 0, 1), List.of(bookingDtoGet));
    }

    @Test
    void getAllOwnerUnsupportedState() {
        assertThrows(UnsupportedState.class, () -> service.getAllOwnerBooking(1, "STATE", 0, 1));
    }

    @Test
    void getAllOwnerBookingNotFoundItem() {
        assertThrows(NotFoundException.class, () -> service.getAllOwnerBooking(1, "ALL", 0, 1));
    }

    @Test
    void getAllOwnerBookingNotFoundUser() {
        Item item = Item.builder()
                .id(0)
                .name("Имя предмета")
                .owner(1)
                .request(1)
                .description("Описание предмета")
                .available(true)
                .build();
        Mockito.when(itemRepository.findByOwner(Mockito.anyInt())).thenReturn(List.of(item));
        assertThrows(NotFoundException.class, () -> service.getAllOwnerBooking(1, "ALL", 0, 1));
    }

    @Test
    void getAllOwnerBookingStateAll() {

        List<Booking> bookingList = new ArrayList<>();
        User user = User.builder()
                .id(0)
                .name("Имя пользователя")
                .email("Почта@gmail.com")
                .build();
        Booking booking = Booking.builder()
                .id(0)
                .itemId(1)
                .start(LocalDateTime.of(2024, 6, 11, 11, 11))
                .end(LocalDateTime.of(2024, 6, 12, 11, 11))
                .status(Status.WAITING)
                .bookerId(0)
                .build();
        Item item = Item.builder()
                .id(0)
                .name("Имя предмета")
                .owner(1)
                .request(1)
                .description("Описание предмета")
                .available(true)
                .build();
        BookerDto bookerDto = BookerDto.builder()
                .id(0)
                .build();
        ItemDtoBooking itemDtoBooking = ItemDtoBooking.builder()
                .id(1)
                .name("Имя предмета")
                .build();
        BookingDtoGet bookingDtoGet = BookingMapper.bookingToBookingDtoGet(booking, bookerDto, itemDtoBooking);
        bookingList.add(booking);
        Mockito.when(itemRepository.findById(Mockito.anyInt())).thenReturn(Optional.ofNullable(item));
        Mockito.when(itemRepository.findByOwner(Mockito.anyInt())).thenReturn(List.of(item));
        Mockito.when(userRepository.findById(Mockito.anyInt())).thenReturn(Optional.ofNullable(user));
        Mockito
                .when(bookingRepository.findByItemIdOrderByStartDesc(Mockito.anyInt(), Mockito.any(PageRequest.class)))
                .thenReturn(bookingList);
        assertEquals(service.getAllOwnerBooking(1, "ALL", 0, 1), List.of(bookingDtoGet));
    }

    @Test
    void getAllOwnerBookingStateWAITING() {
        List<Booking> bookingList = new ArrayList<>();
        User user = User.builder()
                .id(0)
                .name("Имя пользователя")
                .email("Почта@gmail.com")
                .build();
        Booking booking = Booking.builder()
                .id(0)
                .itemId(1)
                .start(LocalDateTime.of(2024, 6, 11, 11, 11))
                .end(LocalDateTime.of(2024, 6, 12, 11, 11))
                .status(Status.WAITING)
                .bookerId(0)
                .build();
        Item item = Item.builder()
                .id(0)
                .name("Имя предмета")
                .owner(1)
                .request(1)
                .description("Описание предмета")
                .available(true)
                .build();
        BookerDto bookerDto = BookerDto.builder()
                .id(0)
                .build();
        ItemDtoBooking itemDtoBooking = ItemDtoBooking.builder()
                .id(1)
                .name("Имя предмета")
                .build();
        BookingDtoGet bookingDtoGet = BookingMapper.bookingToBookingDtoGet(booking, bookerDto, itemDtoBooking);
        bookingList.add(booking);
        Mockito.when(itemRepository.findById(Mockito.anyInt())).thenReturn(Optional.ofNullable(item));
        Mockito.when(itemRepository.findByOwner(Mockito.anyInt())).thenReturn(List.of(item));
        Mockito.when(userRepository.findById(Mockito.anyInt())).thenReturn(Optional.ofNullable(user));
        Mockito
                .when(bookingRepository.findByItemIdAndStatusOrderByStartDesc(Mockito.anyInt(),
                        Mockito.any(Status.class), Mockito.any(PageRequest.class)))
                .thenReturn(bookingList);
        assertEquals(service.getAllOwnerBooking(1, "WAITING", 0, 1), List.of(bookingDtoGet));
    }

    @Test
    void getAllOwnerBookingStateREJECTED() {
        List<Booking> bookingList = new ArrayList<>();
        User user = User.builder()
                .id(0)
                .name("Имя пользователя")
                .email("Почта@gmail.com")
                .build();
        Booking booking = Booking.builder()
                .id(0)
                .itemId(1)
                .start(LocalDateTime.of(2024, 6, 11, 11, 11))
                .end(LocalDateTime.of(2024, 6, 12, 11, 11))
                .status(Status.WAITING)
                .bookerId(0)
                .build();
        Item item = Item.builder()
                .id(0)
                .name("Имя предмета")
                .owner(1)
                .request(1)
                .description("Описание предмета")
                .available(true)
                .build();
        BookerDto bookerDto = BookerDto.builder()
                .id(0)
                .build();
        ItemDtoBooking itemDtoBooking = ItemDtoBooking.builder()
                .id(1)
                .name("Имя предмета")
                .build();
        BookingDtoGet bookingDtoGet = BookingMapper.bookingToBookingDtoGet(booking, bookerDto, itemDtoBooking);
        bookingList.add(booking);
        Mockito.when(itemRepository.findById(Mockito.anyInt())).thenReturn(Optional.ofNullable(item));
        Mockito.when(itemRepository.findByOwner(Mockito.anyInt())).thenReturn(List.of(item));
        Mockito.when(userRepository.findById(Mockito.anyInt())).thenReturn(Optional.ofNullable(user));
        Mockito
                .when(bookingRepository.findByItemIdAndStatusOrderByStartDesc(Mockito.anyInt(),
                        Mockito.any(Status.class), Mockito.any(PageRequest.class)))
                .thenReturn(bookingList);
        assertEquals(service.getAllOwnerBooking(1, "REJECTED", 0, 1), List.of(bookingDtoGet));
    }

    @Test
    void getAllOwnerBookingStateFUTURE() {
        List<Booking> bookingList = new ArrayList<>();
        User user = User.builder()
                .id(0)
                .name("Имя пользователя")
                .email("Почта@gmail.com")
                .build();
        Booking booking = Booking.builder()
                .id(0)
                .itemId(1)
                .start(LocalDateTime.of(2024, 6, 11, 11, 11))
                .end(LocalDateTime.of(2024, 6, 12, 11, 11))
                .status(Status.WAITING)
                .bookerId(0)
                .build();
        Item item = Item.builder()
                .id(0)
                .name("Имя предмета")
                .owner(1)
                .request(1)
                .description("Описание предмета")
                .available(true)
                .build();
        BookerDto bookerDto = BookerDto.builder()
                .id(0)
                .build();
        ItemDtoBooking itemDtoBooking = ItemDtoBooking.builder()
                .id(1)
                .name("Имя предмета")
                .build();
        BookingDtoGet bookingDtoGet = BookingMapper.bookingToBookingDtoGet(booking, bookerDto, itemDtoBooking);
        bookingList.add(booking);
        Mockito.when(itemRepository.findById(Mockito.anyInt())).thenReturn(Optional.ofNullable(item));
        Mockito.when(itemRepository.findByOwner(Mockito.anyInt())).thenReturn(List.of(item));
        Mockito.when(userRepository.findById(Mockito.anyInt())).thenReturn(Optional.ofNullable(user));
        Mockito
                .when(bookingRepository.futureItemId(Mockito.anyInt(), Mockito.any(LocalDateTime.class),
                        Mockito.any(Status.class), Mockito.any(Status.class), Mockito.any(PageRequest.class)))
                .thenReturn(bookingList);
        assertEquals(service.getAllOwnerBooking(1, "FUTURE", 0, 1), List.of(bookingDtoGet));
    }

    @Test
    void getAllOwnerBookingStatePAST() {
        List<Booking> bookingList = new ArrayList<>();
        User user = User.builder()
                .id(0)
                .name("Имя пользователя")
                .email("Почта@gmail.com")
                .build();
        Booking booking = Booking.builder()
                .id(0)
                .itemId(1)
                .start(LocalDateTime.of(2024, 6, 11, 11, 11))
                .end(LocalDateTime.of(2024, 6, 12, 11, 11))
                .status(Status.WAITING)
                .bookerId(0)
                .build();
        Item item = Item.builder()
                .id(0)
                .name("Имя предмета")
                .owner(1)
                .request(1)
                .description("Описание предмета")
                .available(true)
                .build();
        BookerDto bookerDto = BookerDto.builder()
                .id(0)
                .build();
        ItemDtoBooking itemDtoBooking = ItemDtoBooking.builder()
                .id(1)
                .name("Имя предмета")
                .build();
        BookingDtoGet bookingDtoGet = BookingMapper.bookingToBookingDtoGet(booking, bookerDto, itemDtoBooking);
        bookingList.add(booking);
        Mockito.when(itemRepository.findById(Mockito.anyInt())).thenReturn(Optional.ofNullable(item));
        Mockito.when(itemRepository.findByOwner(Mockito.anyInt())).thenReturn(List.of(item));
        Mockito.when(userRepository.findById(Mockito.anyInt())).thenReturn(Optional.ofNullable(user));
        Mockito
                .when(bookingRepository.pasteItemId(Mockito.anyInt(), Mockito.any(LocalDateTime.class),
                        Mockito.any(Status.class), Mockito.any(PageRequest.class)))
                .thenReturn(bookingList);
        assertEquals(service.getAllOwnerBooking(1, "PAST", 0, 1), List.of(bookingDtoGet));
    }

    @Test
    void getAllOwnerBookingStateCURRENT() {
        List<Booking> bookingList = new ArrayList<>();
        User user = User.builder()
                .id(0)
                .name("Имя пользователя")
                .email("Почта@gmail.com")
                .build();
        Booking booking = Booking.builder()
                .id(0)
                .itemId(1)
                .start(LocalDateTime.of(2024, 6, 11, 11, 11))
                .end(LocalDateTime.of(2024, 6, 12, 11, 11))
                .status(Status.WAITING)
                .bookerId(0)
                .build();
        Item item = Item.builder()
                .id(0)
                .name("Имя предмета")
                .owner(1)
                .request(1)
                .description("Описание предмета")
                .available(true)
                .build();
        BookerDto bookerDto = BookerDto.builder()
                .id(0)
                .build();
        ItemDtoBooking itemDtoBooking = ItemDtoBooking.builder()
                .id(1)
                .name("Имя предмета")
                .build();
        BookingDtoGet bookingDtoGet = BookingMapper.bookingToBookingDtoGet(booking, bookerDto, itemDtoBooking);
        bookingList.add(booking);
        Mockito.when(itemRepository.findById(Mockito.anyInt())).thenReturn(Optional.ofNullable(item));
        Mockito.when(itemRepository.findByOwner(Mockito.anyInt())).thenReturn(List.of(item));
        Mockito.when(userRepository.findById(Mockito.anyInt())).thenReturn(Optional.ofNullable(user));
        Mockito
                .when(bookingRepository.currentItemId(Mockito.anyInt(), Mockito.any(LocalDateTime.class),
                        Mockito.any(Status.class), Mockito.any(Status.class), Mockito.any(PageRequest.class)))
                .thenReturn(bookingList);
        assertEquals(service.getAllOwnerBooking(1, "CURRENT", 0, 1), List.of(bookingDtoGet));
    }
}