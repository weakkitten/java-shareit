package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.utility.Status;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@Slf4j
@RequiredArgsConstructor
public class BookingController {
    private final BookingService service;

    @PostMapping
    public Booking createBooking(@RequestHeader("X-Sharer-User-Id") int userId,
                                 @RequestBody @Valid BookingDto bookingDto) {
        return service.createBooking(bookingDto, userId);
    }

    @PatchMapping("/{bookingId}")
    public Booking updateBooking(@RequestHeader("X-Sharer-User-Id") int userId,
                                      @PathVariable int bookingId, @RequestParam Status status) {
        return service.updateBooking(userId, bookingId, status);
    }

    @GetMapping("/{bookingId}")
    public Booking getBooking(@RequestHeader("X-Sharer-User-Id") int userId, @PathVariable int bookingId) {
        return service.getBooking(userId, bookingId);
    }

    @GetMapping
    public List<Booking> getAllUserBooking(@RequestHeader("X-Sharer-User-Id") int bookerId,
                                              @RequestParam Status status) {
        return service.getAllUserBooking(bookerId, status);
    }

    @GetMapping("/{owner}")
    public List<Booking> getAllOwnerBooking(@RequestHeader("X-Sharer-User-Id") int userId,
                                               @PathVariable int bookingId, @RequestParam Status status) {
        return service.getAllOwnerBooking(userId, bookingId, status);
    }
}
