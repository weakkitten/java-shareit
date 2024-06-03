package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.error.exception.BadRequestException;
import ru.practicum.shareit.error.exception.NotFoundException;
import ru.practicum.shareit.error.exception.TimeException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.utility.Status;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingService {
    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    public Booking createBooking(BookingDto dto, int bookerId) {
        Booking booking = BookingMapper.bookingDtoToBooking(dto, bookerId, Status.WAITING);

        if (booking.getEnd().isBefore(booking.getStart())) {
            throw new TimeException("Конец раньше начала");
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
        return bookingRepository.findById(booking.getId()).get();
    }

    public Booking updateBooking(int userId, int bookingId, Status status) {
        Booking booking = bookingRepository.findById(bookingId).get();
        Item item = itemRepository.findById(booking.getItemId()).get();

        if (item.getOwner() != userId) {
            throw new BadRequestException("Может подтвердить только владелец вещи");
        }
        booking.setStatus(status);
        bookingRepository.save(booking);
        return bookingRepository.findById(bookingId).get();
    }

    public Booking getBooking(int userId, int bookingId) {
        Booking booking = bookingRepository.findById(bookingId).get();
        Item item = itemRepository.findById(booking.getItemId()).get();

        if (booking.getBookerId() != userId || item.getOwner() != userId) {
            throw new BadRequestException("Отказано в доступе");
        }
        return bookingRepository.findById(bookingId).get();
    }

    public List<Booking> getAllUserBooking(int bookerId, Status status) {
        return bookingRepository.findByBookerIdAndStatus(bookerId, status);
    }

    public List<BookingDto> getAllOwnerBooking(int ownerId, int bookingId, Status status) {
        List<Item> ownerItemList = itemRepository.findByOwner(ownerId);
        List<BookingDto> bookingDtoList = new ArrayList<>();

        for (Item itemTemp : ownerItemList) {
            if (bookingRepository.findByItemIdAndStatus(itemTemp.getId(), status) != null) {
                bookingDtoList.add(BookingMapper.bookingToBookingDto(bookingRepository
                                  .findByItemIdAndStatus(itemTemp.getId(), status)));
            }
        }
        return bookingDtoList;
    }
}
