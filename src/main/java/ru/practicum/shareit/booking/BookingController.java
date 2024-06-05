package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoGet;
import ru.practicum.shareit.booking.service.BookingService;

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
                                              @RequestParam(defaultValue = "ALL") String state) {
        return service.getAllUserBooking(bookerId, state);
    }

    @GetMapping("/owner")
    public List<BookingDtoGet> getAllOwnerBooking(@RequestHeader("X-Sharer-User-Id") int userId,
                                                @RequestParam(defaultValue = "ALL") String state) {
        System.out.println("ЧЕ У НАС ТУТ? - " + service.getAllOwnerBooking(userId, state));
        System.out.println("ЧЕ У НАС ТУТ? - " + service.getAllOwnerBooking(userId, state));
        System.out.println("ЧЕ У НАС ТУТ? - " + service.getAllOwnerBooking(userId, state));
        System.out.println("ЧЕ У НАС ТУТ? - " + service.getAllOwnerBooking(userId, state));
        System.out.println("ЧЕ У НАС ТУТ? - " + service.getAllOwnerBooking(userId, state));
        System.out.println("ЧЕ У НАС ТУТ? - " + service.getAllOwnerBooking(userId, state));
        return service.getAllOwnerBooking(userId, state);
    }
}
