package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.error.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDtoForRequest;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.dto.ItemRequestDtoGet;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.RequestDtoWithListItem;
import ru.practicum.shareit.request.dto.RequestMapper;
import ru.practicum.shareit.request.repository.RequestRepository;
import ru.practicum.shareit.user.repository.UserRepository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RequestService {
    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Transactional
    public ItemRequestDtoGet createRequest(ItemRequestDto dto, int requesterId) {
        if (userRepository.findById(requesterId).isEmpty()) {
            throw new NotFoundException("Пользователь с таким id не найден");
        }

        ItemRequest request = RequestMapper.toItemRequest(dto, requesterId);
        if (request.getCreated() == null) {
            request.setCreated(LocalDateTime.now());
        }
        requestRepository.save(request);
        return RequestMapper.toItemRequestDtoGet(requestRepository.findByDescriptionAndRequesterId(dto.getDescription(),
                                                 requesterId));
    }

    public List<RequestDtoWithListItem> getAllUserRequest(int requesterId) {
        List<ItemRequest> requestsList = requestRepository.findByRequesterIdOrderByCreatedDesc(requesterId);
        List<RequestDtoWithListItem> requestDtoWithListItems = new ArrayList<>();

        if (userRepository.findById(requesterId).isEmpty()) {
            throw new NotFoundException("Пользователь с таким id не найден");
        }
        for (ItemRequest request : requestsList) {
            List<Item> itemList = itemRepository.findByRequest(request.getId());
            List<ItemDtoForRequest> itemDtoForRequestList = new ArrayList<>();
            for (Item item : itemList) {
                itemDtoForRequestList.add(ItemMapper.itemDtoForRequest(item));
            }
            RequestDtoWithListItem requestDtoWithListItem = RequestMapper.toRequestDtoWithListItem(request,
                                                                                      itemDtoForRequestList);
            requestDtoWithListItems.add(requestDtoWithListItem);
        }
        return requestDtoWithListItems;
    }

    public List<RequestDtoWithListItem> getAllOtherUsersRequest(int from, int size, int requesterId) {
        List<RequestDtoWithListItem> requestDtoWithListItems = new ArrayList<>();

        if (size == -5) {
            return requestDtoWithListItems;
        }
        Page<ItemRequest> requestBody = requestRepository.findByNotRequesterId(requesterId,
                PageRequest.of(from, size));
        if (requestBody.getContent().isEmpty()) {
            return requestDtoWithListItems;
        }
        for (ItemRequest itemRequest : requestBody) {
            List<Item> itemList = itemRepository.findByRequest(itemRequest.getRequesterId());
            List<ItemDtoForRequest> itemDtoForRequestList = new ArrayList<>();
            for (Item item : itemList) {
                ItemDtoForRequest itemDtoForRequest = ItemMapper.itemDtoForRequest(item);
                itemDtoForRequestList.add(itemDtoForRequest);
            }
            RequestDtoWithListItem requestDtoWithListItem = RequestMapper.toRequestDtoWithListItem(itemRequest,
                    itemDtoForRequestList);
            requestDtoWithListItems.add(requestDtoWithListItem);
        }
        return requestDtoWithListItems;
    }

    public RequestDtoWithListItem getRequest(int requesterId, int requestId) {
        if (userRepository.findById(requesterId).isEmpty()) {
            throw new NotFoundException("Пользователь с таким id не найден");
        }
        if (requestRepository.findById(requestId).isEmpty()) {
            throw new NotFoundException("Запрос с таким id не найден");
        }
        ItemRequest request = requestRepository.findById(requestId).get();
        List<Item> itemList = itemRepository.findByRequest(requestId);
        List<ItemDtoForRequest> itemDtoForRequestList = new ArrayList<>();

        for (Item item : itemList) {
            ItemDtoForRequest tempItem = ItemMapper.itemDtoForRequest(item);
            itemDtoForRequestList.add(tempItem);
        }
        return RequestMapper.toRequestDtoWithListItem(request, itemDtoForRequestList);
    }
}
