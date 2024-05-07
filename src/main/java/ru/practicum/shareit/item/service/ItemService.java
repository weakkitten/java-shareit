package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.error.exception.BadRequestException;
import ru.practicum.shareit.error.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoGet;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.InMemoryItemRepository;
import ru.practicum.shareit.user.repository.InMemoryUserRepository;

import java.util.ArrayList;
import java.util.List;

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

    public Item updateItem(int itemId, int id, ItemDto itemDto) {
        Item itemRep = itemRepository.getItem(itemId);

        itemDto.setId(itemId);
        if (itemRep.getOwner() != id) {
            throw new NotFoundException("Этот пользователь не имеет прав для редактирования");
        }
        if (itemDto.getName() == null) {
            itemDto.setName(itemRep.getName());
        }
        if (itemDto.getDescription() == null) {
            itemDto.setDescription(itemRep.getDescription());
        }
        if (itemDto.getAvailable() == null) {
            itemDto.setAvailable(itemRep.isAvailable());
        }
        itemRepository.updateItem(itemId, ItemDto.toItem(id, itemDto));
        return itemRepository.getItem(id);
    }

    public Item getItem(int itemId) {
        return itemRepository.getItem(itemId);
    }

    public List<ItemDtoGet> getAllUserItems(int userId) {
        List<Item> allItems = itemRepository.returnItemList();
        List<ItemDtoGet> userList = new ArrayList<>();
        for (Item item : allItems) {
            if (item.getOwner() == userId) {
                userList.add(ItemDtoGet.toItemDtoGet(item));
            }
        }
        return userList;
    }

    public List<ItemDtoGet> searchItem(String text) {
        List<Item> allItems = itemRepository.returnItemList();
        List<ItemDtoGet> itemsList = new ArrayList<>();
        for (Item item : allItems) {
            if (item.getName().equals(text) && item.isAvailable()) {
                itemsList.add(ItemDtoGet.toItemDtoGet(item));
            }else if (item.getDescription().contains(text) && item.isAvailable()) {
                itemsList.add(ItemDtoGet.toItemDtoGet(item));
            }
        }
        return itemsList;
    }
}
