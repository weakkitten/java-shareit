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
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.utility.Status;

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
        if (!itemRepository.findById(booking.getItemId()).get().isAvailable()) {
            throw new BadRequestException("Предмет занят");
        }
        if (userRepository.findById(bookerId).isEmpty()) {
            throw new NotFoundException("Не существует пользователя с таким id");
        }
        bookingRepository.save(booking);
        return bookingRepository.findById(booking.getId()).get();
    }
}
