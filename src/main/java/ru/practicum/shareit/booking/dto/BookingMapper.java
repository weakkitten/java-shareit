package ru.practicum.shareit.booking.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.utility.Status;

import java.time.LocalDateTime;

@UtilityClass
public class BookingMapper {
    public static Booking bookingDtoToBooking(BookingDto dto, int userId, Status status) {
        return Booking.builder()
                .start(LocalDateTime.now())
                .itemId(dto.getItemId())
                .bookerId(userId)
                .status(status)
                .build();
    }

    public static BookingDto bookingToBookingDto(Booking booking) {
        return BookingDto.builder()
                .itemId(booking.getItemId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .build();
    }
}
