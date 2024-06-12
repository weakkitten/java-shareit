package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.*;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.error.exception.*;
import ru.practicum.shareit.item.dto.ItemDtoBooking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.utility.State;
import ru.practicum.shareit.utility.Status;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingService {
    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Transactional
    public BookingDtoGet createBooking(BookingDto dto, int bookerId) {
        Booking booking = BookingMapper.bookingDtoToBooking(dto, bookerId, Status.WAITING);

        if (booking.getEnd().isBefore(booking.getStart())) {
            throw new TimeException("Конец раньше начала");
        }
        if (booking.getEnd().equals(booking.getStart())) {
            throw new TimeException("Время начала и конца равны");
        }
        if (itemRepository.findById(booking.getItemId()).isEmpty()) {
            throw new NotFoundException("Неккоректный id предмета");
        }
        if (!itemRepository.findById(booking.getItemId()).get().isAvailable()) {
            throw new BadRequestException("Предмет занят");
        }
        if (userRepository.findById(bookerId).isEmpty()) {
            throw new NotFoundException("Не существует пользователя с таким id");
        }
        if (itemRepository.findById(booking.getItemId()).get().getOwner() == bookerId) {
            throw new BookingReject("Нельзя взять в аренду у самого себя");
        }
        bookingRepository.save(booking);
        BookerDto bookerDto = BookerDto.builder()
                                       .id(bookerId)
                                       .build();
        ItemDtoBooking itemDtoBooking = ItemDtoBooking.builder()
                .id(booking.getItemId())
                .name(itemRepository.findById(booking.getItemId()).get().getName())
                .build();
        return BookingMapper.bookingToBookingDtoGet(bookingRepository.findById(booking.getId()).get(),
                                                    bookerDto, itemDtoBooking);
    }

    @Transactional
    public BookingDtoGet updateBooking(int userId, int bookingId, Boolean status) {
        Booking booking = bookingRepository.findById(bookingId).get();
        Item item = itemRepository.findById(booking.getItemId()).get();

        if (item.getOwner() != userId) {
            throw new BookingReject("Может подтвердить только владелец вещи");
        }
        if (booking.getStatus() == Status.APPROVED && status) {
            throw new BadRequestException("Некорректное действие");
        }
        if (status) {
            booking.setStatus(Status.APPROVED);
        } else {
            booking.setStatus(Status.REJECTED);
        }
        bookingRepository.save(booking);
        BookerDto bookerDto = BookerDto.builder()
                .id(booking.getBookerId())
                .build();
        ItemDtoBooking itemDtoBooking = ItemDtoBooking.builder()
                .id(booking.getItemId())
                .name(itemRepository.findById(booking.getItemId()).get().getName())
                .build();
        return BookingMapper.bookingToBookingDtoGet(bookingRepository.findById(booking.getId()).get(),
                bookerDto, itemDtoBooking);
    }

    public BookingDtoGet getBooking(int userId, int bookingId) {
        if (bookingRepository.findById(bookingId).isEmpty()) {
            throw new NotFoundException("Объект не найден");
        }
        Booking booking = bookingRepository.findById(bookingId).get();
        Item item = itemRepository.findById(booking.getItemId()).get();

        if ((booking.getBookerId() != userId) && (item.getOwner() != userId)) {
            throw new BookingReject("Отказано в доступе");
        }
        BookerDto bookerDto = BookerDto.builder()
                .id(booking.getBookerId())
                .build();
        ItemDtoBooking itemDtoBooking = ItemDtoBooking.builder()
                .id(booking.getItemId())
                .name(itemRepository.findById(booking.getItemId()).get().getName())
                .build();
        return BookingMapper.bookingToBookingDtoGet(bookingRepository.findById(booking.getId()).get(),
                bookerDto, itemDtoBooking);
    }

    public List<BookingDtoGet> getAllUserBooking(int bookerId, String state, int page, int size) {
        if (page < 0) throw new RuntimeException();
        List<Booking> bookingList = null;
        List<BookingDtoGet> bookingDtoList = new ArrayList<>();

        if (userRepository.findById(bookerId).isEmpty()) {
            throw new NotFoundException("Не существует пользователя с таким id");
        }
        try {
            State.valueOf(state);
        } catch (IllegalArgumentException e) {
            throw new UnsupportedState("Unknown state: UNSUPPORTED_STATUS");
        }

        if (State.valueOf(state) == State.ALL) {
            System.out.println("Мы тут?");
            bookingList = new ArrayList<>(bookingRepository.findByBookerIdOrderByStartDesc(bookerId,
                                                            PageRequest.of(page / size, size)));
        } else if (State.valueOf(state) == State.WAITING) {
            bookingList = new ArrayList<>(bookingRepository.findByBookerIdAndStatusOrderByStartDesc(bookerId,
                    Status.WAITING, PageRequest.of(page / size, size)));
        } else if (State.valueOf(state) == State.REJECTED) {
            bookingList = new ArrayList<>(bookingRepository.findByBookerIdAndStatusOrderByStartDesc(bookerId,
                    Status.REJECTED, PageRequest.of(page / size, size)));
        } else if (State.valueOf(state) == State.FUTURE) {
            bookingList = new ArrayList<>(bookingRepository.future(bookerId, LocalDateTime.now(), Status.APPROVED,
                    Status.WAITING, PageRequest.of(page / size, size)));
        } else if (State.valueOf(state) == State.PAST) {
            bookingList = new ArrayList<>(bookingRepository.paste(bookerId, LocalDateTime.now(), Status.APPROVED,
                    PageRequest.of(page / size, size)));
        } else if (State.valueOf(state) == State.CURRENT) {
            bookingList = new ArrayList<>(bookingRepository.current(bookerId, LocalDateTime.now(),
                    Status.APPROVED, Status.REJECTED, PageRequest.of(page / size, size)));
        }
        for (Booking booking : bookingList) {
            BookerDto bookerDto = BookerDto.builder()
                    .id(booking.getBookerId())
                    .build();
            ItemDtoBooking itemDtoBooking = ItemDtoBooking.builder()
                    .id(booking.getItemId())
                    .name(itemRepository.findById(booking.getItemId()).get().getName())
                    .build();
            bookingDtoList.add(BookingMapper.bookingToBookingDtoGet(booking, bookerDto, itemDtoBooking));
        }
        return bookingDtoList;
    }

    public List<BookingDtoGet> getAllOwnerBooking(int ownerId, String state, int page, int size) {
        if (page < 0) throw new RuntimeException();
        List<Item> ownerItemList = itemRepository.findByOwner(ownerId);
        List<BookingDtoGet> bookingDtoList = new ArrayList<>();

        try {
            State.valueOf(state);
        } catch (IllegalArgumentException e) {
            throw new UnsupportedState("Unknown state: UNSUPPORTED_STATUS");
        }
        State stateEnum = State.valueOf(state);
        if (userRepository.findById(ownerId).isEmpty()) {
            throw new NotFoundException("Не существует пользователя с таким id");
        }
        for (Item itemTemp : ownerItemList) {
            List<Booking> bookingList = null;
            int itemId = itemTemp.getId();

            if (stateEnum.equals(State.ALL)) {
                bookingList = new ArrayList<>(bookingRepository.findByItemIdOrderByStartDesc(itemId,
                        PageRequest.of(page / size, size)));
            } else if (stateEnum.equals(State.WAITING)) {
                bookingList = new ArrayList<>(bookingRepository.findByItemIdAndStatusOrderByStartDesc(itemId,
                        Status.WAITING,
                        PageRequest.of(page / size, size)));
            } else if (stateEnum.equals(State.REJECTED)) {
                bookingList = new ArrayList<>(bookingRepository.findByItemIdAndStatusOrderByStartDesc(itemId,
                        Status.REJECTED,
                        PageRequest.of(page / size, size)));
            } else if (stateEnum.equals(State.FUTURE)) {
                bookingList = new ArrayList<>(bookingRepository.futureItemId(itemId, LocalDateTime.now(), Status.APPROVED,
                        Status.WAITING,
                        PageRequest.of(page / size, size)));
            } else if (stateEnum.equals(State.PAST)) {
                bookingList = new ArrayList<>(bookingRepository.pasteItemId(itemId, LocalDateTime.now(),
                        Status.APPROVED,
                        PageRequest.of(page / size, size)));
            } else if (stateEnum.equals(State.CURRENT)) {
                bookingList = new ArrayList<>(bookingRepository.currentItemId(itemId, LocalDateTime.now(),
                        Status.APPROVED, Status.REJECTED,
                        PageRequest.of(page / size, size)));
            }
            for (Booking booking : bookingList) {
                BookerDto bookerDto = BookerDto.builder()
                        .id(booking.getBookerId())
                        .build();
                ItemDtoBooking itemDtoBooking = ItemDtoBooking.builder()
                        .id(booking.getItemId())
                        .name(itemRepository.findById(booking.getItemId()).get().getName())
                        .build();
                bookingDtoList.add(BookingMapper.bookingToBookingDtoGet(booking, bookerDto, itemDtoBooking));
            }
        }
        return bookingDtoList;
    }
}
