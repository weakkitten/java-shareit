package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.utility.Status;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {
    public List<Booking> findByBookerIdAndStatusOrderByStartDesc(int bookerId, Status status);
    public Booking findByItemIdAndStatus(int itemId, Status status);
    public List<Booking> findByBookerIdOrderByStartDesc(int bookerId);
    public Booking findByBookerIdAndItemId(int bookerId, int itemId);
    @Query("select b from Booking b " +
            "where b.bookerId = ?1" +
            " and b.status = approved" +
            " and ?2 < b.start" +
            " order by b.start desc")
    public List<Booking> future(int bookerId, LocalDateTime now);
    @Query("select b from Booking b " +
            "where b.bookerId = ?1" +
            " and b.status = approved" +
            " and b.end < ?2" +
            "order by b.start desc")
    public List<Booking> paste(int bookerId, LocalDateTime now);
    @Query("select b from Booking b " +
            "where b.bookerId = ?1" +
            " and b.status = approved" +
            " and b.start <= ?2" +
            " and b.end > ?2" +
            " order by b.start desc")
    public List<Booking> current(int bookerId, LocalDateTime now);
}
