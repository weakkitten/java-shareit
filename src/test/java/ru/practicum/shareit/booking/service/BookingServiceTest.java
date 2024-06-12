package ru.practicum.shareit.booking.service;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.repository.UserRepository;

import static org.junit.jupiter.api.Assertions.*;

class BookingServiceTest {
    @InjectMocks
    private BookingService service;

    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private UserRepository userRepository;

    @Test
    void createBooking() {
    }

    @Test
    void updateBooking() {
    }

    @Test
    void getBooking() {
    }

    @Test
    void getAllUserBooking() {
    }

    @Test
    void getAllOwnerBooking() {
    }
}