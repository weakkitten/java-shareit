package ru.practicum.shareit.request.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.request.ItemRequest;

import java.util.List;

public interface RequestRepository extends JpaRepository<ItemRequest, Integer> {
    public ItemRequest findByDescriptionAndRequesterId(String text, int requesterId);
    public List<ItemRequest> findByRequesterIdOrderByCreatedDesc(int requesterId);
    @Query("select r from ItemRequest r " +
            "where r.requesterId <> ?1")
    public Page<ItemRequest> findByNotRequesterId(int requesterId, Pageable pageable);
}
