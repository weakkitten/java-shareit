package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.repository.BookingRepository;

@Service
@RequiredArgsConstructor
public class BookingService {
    private final BookingRepository bookingRepository;

    public BookingDto createBooking(BookingDto dto, int userId) {
        bookingRepository.save(BookingMapper.bookingDtoToBooking(dto, userId));
        return BookingMapper.bookingToBookingDto(bookingRepository.findById(dto.getId()).get());
    }
}
