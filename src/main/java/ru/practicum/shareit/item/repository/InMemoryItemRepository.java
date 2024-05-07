package ru.practicum.shareit.item.repository;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.HashMap;
import java.util.Map;

@Data
@RequiredArgsConstructor
@Repository
public class InMemoryItemRepository {
    private final Map<Integer, Item> itemRepository = new HashMap<>();

    public void createItem(int id, Item item) {
        itemRepository.put(id, item);
    }

    public Item getItem(int id) {
        return itemRepository.get(id);
    }
}
