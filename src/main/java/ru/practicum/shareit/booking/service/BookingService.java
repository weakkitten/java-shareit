package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.*;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.error.exception.BadRequestException;
import ru.practicum.shareit.error.exception.NotFoundException;
import ru.practicum.shareit.error.exception.TimeException;
import ru.practicum.shareit.item.dto.ItemDtoBooking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.utility.State;
import ru.practicum.shareit.utility.Status;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingService {
    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

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

    public BookingDtoGet updateBooking(int userId, int bookingId, Boolean status) {
        Booking booking = bookingRepository.findById(bookingId).get();
        Item item = itemRepository.findById(booking.getItemId()).get();

        if (item.getOwner() != userId) {
            throw new BadRequestException("Может подтвердить только владелец вещи");
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
        Booking booking = bookingRepository.findById(bookingId).get();
        Item item = itemRepository.findById(booking.getItemId()).get();
        System.out.println("Пользователь - " + userId);
        System.out.println("Букер - " + booking.getBookerId());
        System.out.println("Хозяин вещи - " + item.getOwner());
        System.out.println(booking.getBookerId() != userId);
        System.out.println(item.getOwner() != userId);
        System.out.println(booking.getBookerId() != userId || item.getOwner() != userId);

        if ((booking.getBookerId() != userId) && (item.getOwner() != userId)) {
            throw new BadRequestException("Отказано в доступе");
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

    public List<BookingDtoGet> getAllUserBooking(int bookerId, State state) {
        List<Booking> bookingList;
        List<BookingDtoGet> bookingDtoList = new ArrayList<>();

        if (userRepository.findById(bookerId).isEmpty()) {
            throw new NotFoundException("Не существует пользователя с таким id");
        }
        if (state == State.ALL) {
            bookingList = new ArrayList<>(bookingRepository.findByBookerIdOrderByStartDesc(bookerId));
        } else {
            bookingList = new ArrayList<>(bookingRepository.findByBookerIdAndStatusOrderByStartDesc(bookerId, status));
        }
        System.out.println("Список - " + bookingList);
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

    public List<BookingDto> getAllOwnerBooking(int ownerId, int bookingId, State state) {
        List<Item> ownerItemList = itemRepository.findByOwner(ownerId);
        List<BookingDto> bookingDtoList = new ArrayList<>();
        Status status = null;

        if (userRepository.findById(ownerId).isEmpty()) {
            throw new NotFoundException("Не существует пользователя с таким id");
        }
        for (Item itemTemp : ownerItemList) {
            if (bookingRepository.findByItemIdAndStatus(itemTemp.getId(), status) != null) {
                bookingDtoList.add(BookingMapper.bookingToBookingDto(bookingRepository
                                  .findByItemIdAndStatus(itemTemp.getId(), status)));
            }
        }
        return bookingDtoList;
    }
}
