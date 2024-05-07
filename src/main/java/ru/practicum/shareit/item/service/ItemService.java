package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.error.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.InMemoryItemRepository;
import ru.practicum.shareit.user.repository.InMemoryUserRepository;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final InMemoryItemRepository itemRepository;
    private final InMemoryUserRepository userRepository;
    private static int itemCount;

    public Item createItem(int id, ItemDto itemDto) {
        if (userRepository.getUser(id) == null) {
            throw new NotFoundException("Нет такого пользователя");
        }
        if(itemDto.getId() == 0) {
            itemCount++;
            itemDto.setId(itemCount);
        }else {
            itemDto.setId(itemDto.getId() + 1);
            itemCount++;
        }
        itemRepository.createItem(id, ItemDto.toItem(id, itemDto));
        return itemRepository.getItem(id);
    }
}
