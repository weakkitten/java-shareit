package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.utility.Status;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {
    public List<Booking> findByBookerIdAndStatusOrderByStartDesc(int bookerId, Status status);
    public Booking findByItemIdAndStatus(int itemId, Status status);
    public List<Booking> findByBookerIdOrderByStartDesc(int bookerId);
    public Booking findByBookerIdAndItemId(int bookerId, int itemId);
}
