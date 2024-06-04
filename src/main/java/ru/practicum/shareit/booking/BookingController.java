package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoGet;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.utility.State;
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
    public BookingDtoGet createBooking(@RequestHeader("X-Sharer-User-Id") int userId,
                                       @RequestBody @Valid BookingDto bookingDto) {
        return service.createBooking(bookingDto, userId);
    }

    @PatchMapping("/{bookingId}")
    public BookingDtoGet updateBooking(@RequestHeader("X-Sharer-User-Id") int userId,
                                          @PathVariable int bookingId, @RequestParam Boolean approved) {
        return service.updateBooking(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingDtoGet getBooking(@RequestHeader("X-Sharer-User-Id") int userId, @PathVariable int bookingId) {
        return service.getBooking(userId, bookingId);
    }

    @GetMapping
    public List<BookingDtoGet> getAllUserBooking(@RequestHeader("X-Sharer-User-Id") int bookerId,
                                              @RequestParam(defaultValue = "ALL") State state) {
        return service.getAllUserBooking(bookerId, state);
    }

    @GetMapping("/owner")
    public List<BookingDto> getAllOwnerBooking(@RequestHeader("X-Sharer-User-Id") int userId,
                                               @PathVariable int bookingId, @RequestParam State state) {
        return service.getAllOwnerBooking(userId, bookingId, state);
    }
}
