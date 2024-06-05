package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.utility.Status;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {
    public List<Booking> findByBookerIdAndStatusOrderByStartDesc(int bookerId, Status status);

    public List<Booking> findByItemIdAndStatusOrderByStartDesc(int itemId, Status status);

    public List<Booking> findByItemIdOrderByStartDesc(int itemId);

    public List<Booking> findByBookerIdOrderByStartDesc(int bookerId);

    public List<Booking> findByBookerIdAndItemIdAndStatusAndEndLessThan(int bookerId, int itemId, Status status,
                                                                        LocalDateTime time);

    @Query("select b from Booking b " +
            "where b.bookerId = ?1" +
            " and (b.status = ?3" +
            " or b.status = ?4)" +
            " and ?2 < b.start" +
            " order by b.start desc")
    public List<Booking> future(int bookerId, LocalDateTime now, Status app, Status wait);

    @Query("select b from Booking b " +
            "where b.bookerId = ?1" +
            " and b.status = ?3" +
            " and b.end < ?2" +
            " order by b.start desc")
    public List<Booking> paste(int bookerId, LocalDateTime now, Status status);

    @Query("select b from Booking b " +
            "where b.bookerId = ?1" +
            " and (b.status = ?3" +
            " or b.status = ?4)" +
            " and b.start <= ?2" +
            " and b.end > ?2" +
            " order by b.start desc")
    public List<Booking> current(int bookerId, LocalDateTime now, Status status, Status rej);

    @Query("select b from Booking b " +
            "where b.itemId = ?1" +
            " and (b.status = ?3" +
            " or b.status = ?4)" +
            " and ?2 < b.start" +
            " order by b.start desc")
    public List<Booking> futureItemId(int itemId, LocalDateTime now, Status app, Status wait);

    @Query("select b from Booking b " +
            "where b.itemId = ?1" +
            " and b.status = ?3" +
            " and b.end < ?2" +
            " order by b.start desc")
    public List<Booking> pasteItemId(int itemId, LocalDateTime now, Status status);

    @Query("select b from Booking b " +
            "where b.itemId = ?1" +
            " and (b.status = ?3" +
            " or b.status = ?4)" +
            " and b.start <= ?2" +
            " and b.end > ?2" +
            " order by b.start desc")
    public List<Booking> currentItemId(int itemId, LocalDateTime now, Status status, Status rej);

    @Query("select b from Booking b " +
            "where b.itemId = ?1" +
            " and b.status = ?2" +
            " and (b.end < ?3" +
            " or (b.start < ?3 and b.end > ?3))" +
            " order by b.start desc")
    public List<Booking> lastBooking(int itemId, Status status, LocalDateTime time);

    @Query("select b from Booking b " +
            "where b.itemId = ?1" +
            " and b.status = ?2" +
            " and b.start > ?3" +
            " order by b.start asc")
    public List<Booking> nextBooking(int itemId,Status status, LocalDateTime time);

    public Booking findByItemIdAndStatusAndEndLessThan(int itemId, Status status, LocalDateTime time);
}
