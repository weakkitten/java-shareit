package ru.practicum.shareit.request.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.request.ItemRequest;

import java.util.List;

public interface RequestRepository extends JpaRepository<ItemRequest, Integer> {
    ItemRequest findByDescriptionAndRequesterId(String text, int requesterId);

    List<ItemRequest> findByRequesterIdOrderByCreatedDesc(int requesterId);

    @Query("select r from ItemRequest r " +
            "where r.requesterId <> ?1")
    List<ItemRequest> findByNotRequesterId(int requesterId, Pageable pageable);
}
