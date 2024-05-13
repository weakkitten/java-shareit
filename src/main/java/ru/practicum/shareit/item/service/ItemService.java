package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.error.exception.BadRequestException;
import ru.practicum.shareit.error.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoGet;
import ru.practicum.shareit.item.dto.ItemMapper;
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

    public ItemDtoGet createItem(int userId, ItemDto itemDto) {
        if (userRepository.getUser(userId) == null) {
            throw new NotFoundException("Нет такого пользователя");
        }
        itemRepository.setItemCount(itemRepository.getItemCount() + 1);
        itemDto.setId(itemRepository.getItemCount());
        itemRepository.createItem(itemRepository.getItemCount(), ItemMapper.toItem(userId, itemDto));
        return ItemMapper.toItemDtoGet(itemRepository.getItem(itemRepository.getItemCount()));
    }

    public ItemDtoGet updateItem(int itemId, int userId, ItemDto itemDto) {
        Item itemRep = itemRepository.getItem(itemId);

        if (itemRep.getOwner() != userId) {
            throw new NotFoundException("Этот пользователь не имеет прав для редактирования");
        }
        if (itemDto.getName() != null) {
            itemRep.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null) {
            itemRep.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            itemRep.setAvailable(itemDto.getAvailable());
        }
        itemRepository.updateItem(itemId, itemRep);
        return ItemMapper.toItemDtoGet(itemRepository.getItem(itemId));
    }

    public ItemDtoGet getItem(int itemId) {
        return ItemMapper.toItemDtoGet(itemRepository.getItem(itemId));
    }

    public List<ItemDtoGet> getAllUserItems(int userId) {
        List<Item> allItems = itemRepository.returnItemList();
        List<ItemDtoGet> userList = new ArrayList<>();
        for (Item item : allItems) {
            if (item.getOwner() == userId) {
                userList.add(ItemMapper.toItemDtoGet(item));
            }
        }
        return userList;
    }

    public List<ItemDtoGet> searchItem(String text) {
        List<ItemDtoGet> itemsList = new ArrayList<>();
        if (!text.isBlank()) {
            List<Item> allItems = itemRepository.returnItemList();
            for (Item item : allItems) {
                if ((item.getName().toLowerCase().contains(text.toLowerCase())
                        || item.getDescription().toLowerCase().contains(text.toLowerCase())) &&  item.isAvailable()) {
                    System.out.println("Попал сюда");
                    itemsList.add(ItemMapper.toItemDtoGet(item));
                }
            }
        }
        return itemsList;
    }
}
