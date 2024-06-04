package ru.practicum.shareit.booking.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.ItemDtoBooking;
import ru.practicum.shareit.utility.Status;

import java.time.LocalDateTime;

@UtilityClass
public class BookingMapper {
    public static Booking bookingDtoToBooking(BookingDto dto, int userId, Status status) {
        return Booking.builder()
                .itemId(dto.getItemId())
                .start(dto.getStart())
                .end(dto.getEnd())
                .bookerId(userId)
                .status(status)
                .build();
    }

    public static BookingDto bookingToBookingDto(Booking booking) {
        return BookingDto.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .itemId(booking.getItemId())
                .bookerId(booking.getBookerId())
                .status(booking.getStatus())
                .build();
    }

    public static BookingDtoGet bookingToBookingDtoGet(Booking booking, BookerDto bookerDto, ItemDtoBooking itemDtoBooking) {
        return BookingDtoGet.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .booker(bookerDto)
                .item(itemDtoBooking)
                .status(booking.getStatus())
                .build();
    }
}
