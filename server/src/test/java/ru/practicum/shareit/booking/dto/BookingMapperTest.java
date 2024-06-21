package ru.practicum.shareit.booking.dto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.utility.Status;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class BookingMapperTest {
    Booking booking = Booking.builder()
            .id(0)
            .itemId(0)
            .start(LocalDateTime.of(2024, 6, 11, 15, 15))
            .end(LocalDateTime.of(2024, 6, 11, 15, 30))
            .bookerId(1)
            .status(Status.APPROVED)
            .build();
    BookingDto bookingDto = BookingDto.builder()
            .id(booking.getId())
            .start(booking.getStart())
            .end(booking.getEnd())
            .itemId(booking.getItemId())
            .bookerId(booking.getBookerId())
            .status(booking.getStatus())
            .build();

    BookingDtoGet dtoGet = BookingDtoGet.builder()
            .id(booking.getId())
            .start(booking.getStart())
            .end(booking.getEnd())
            .booker(null)
            .item(null)
            .status(booking.getStatus())
            .build();

    BookingDtoItem dtoItem = BookingDtoItem.builder()
            .id(booking.getId())
            .bookerId(booking.getBookerId())
            .build();

    @Test
    void bookingDtoToBooking() {
        Booking bookingTest = BookingMapper.bookingDtoToBooking(bookingDto, 1, Status.APPROVED);

        Assertions.assertEquals(bookingTest, booking);
    }

    @Test
    void bookingToBookingDto() {
        BookingDto dto = BookingMapper.bookingToBookingDto(booking);

        Assertions.assertEquals(dto, bookingDto);
    }

    @Test
    void bookingToBookingDtoGet() {
        BookingDtoGet bookingDtoGet = BookingMapper.bookingToBookingDtoGet(booking, null, null);

        Assertions.assertEquals(bookingDtoGet, dtoGet);
    }

    @Test
    void toBookingDtoItem() {
        BookingDtoItem bookingDtoItem = BookingMapper.toBookingDtoItem(booking);

        Assertions.assertEquals(bookingDtoItem, dtoItem);
    }
}