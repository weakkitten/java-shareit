package ru.practicum.shareit.booking.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.booking.model.Booking;

import java.time.LocalDateTime;

@UtilityClass
public class BookingMapper {
    public static Booking bookingDtoToBooking(BookingDto dto, int userId) {
        return Booking.builder()
                .id(dto.getId())
                .start(LocalDateTime.now())
                .item_id(dto.getItem_id())
                .booker_id(userId)
                .status(dto.getStatus())
                .build();
    }

    public static BookingDto bookingToBookingDto(Booking booking) {
        return BookingDto.builder()
                .id(booking.getId())
                .item_id(booking.getItem_id())
                .booker_id(booking.getBooker_id())
                .status(booking.getStatus())
                .build();
    }
}
