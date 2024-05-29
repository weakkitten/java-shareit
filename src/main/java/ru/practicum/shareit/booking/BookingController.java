package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
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
    public BookingDto createBooking(@RequestHeader("X-Sharer-User-Id") int userId,
                                    @RequestBody @Valid BookingDto bookingDto) {
        return null;
    }

    @PatchMapping("/{bookingId}")
    public BookingDto approvedBooking(@RequestHeader("X-Sharer-User-Id") int userId,
                                      @PathVariable int bookingId, @RequestParam Status approved) {
        return null;
    }

    @GetMapping("/{bookingId}")
    public BookingDto getBooking(@RequestHeader("X-Sharer-User-Id") int userId, @PathVariable int bookingId) {
        return null;
    }

    @GetMapping
    public List<BookingDto> getAllUserBooking(@RequestHeader("X-Sharer-User-Id") int userId,
                                              @RequestParam Status approved) {
        return null;
    }

    @GetMapping("/{owner}")
    public List<BookingDto> getAllOwnerBooking(@PathVariable int bookingId, @RequestParam Status approved) {
        return null;
    }
}
