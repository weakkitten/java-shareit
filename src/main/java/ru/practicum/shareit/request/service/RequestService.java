package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
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
import java.util.HashMap;
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
        List<Integer> requestIdList = new ArrayList<>();
        List<RequestDtoWithListItem> requestDtoWithListItem = new ArrayList<>();

        if (userRepository.findById(requesterId).isEmpty()) {
            throw new NotFoundException("Пользователь с таким id не найден");
        }
        for (ItemRequest request : requestsList) {
            requestIdList.add(request.getId());
        }
        List<Item> itemList = itemRepository.findByRequestIn(requestIdList);
        if (!itemList.isEmpty()) {
            HashMap<Integer, List<ItemDtoForRequest>> itemDtoForRequestMap = new HashMap<>();
            for (Item item : itemList) {
                if (itemDtoForRequestMap.containsKey(item.getRequest())) {
                    itemDtoForRequestMap.get(item.getRequest()).add(ItemMapper.itemDtoForRequest(item));
                } else {
                    List<ItemDtoForRequest> tempList = new ArrayList<>();
                    tempList.add(ItemMapper.itemDtoForRequest(item));
                    itemDtoForRequestMap.put(item.getRequest(), tempList);
                }
            }
            for (ItemRequest itemRequest : requestsList) {
                requestDtoWithListItem.add(RequestMapper.toRequestDtoWithListItem(itemRequest,
                        itemDtoForRequestMap.get(itemRequest.getId())));
            }
            return requestDtoWithListItem;
        } else {
            for (ItemRequest itemRequest : requestsList) {
                requestDtoWithListItem.add(RequestMapper.toRequestDtoWithListItem(itemRequest,
                        new ArrayList<>()));
            }
            return requestDtoWithListItem;
        }
    }

    public List<RequestDtoWithListItem> getAllOtherUsersRequest(int from, int size, int requesterId) {
        List<RequestDtoWithListItem> requestDtoWithListItem = new ArrayList<>();

        if (size == -5) {
            return requestDtoWithListItem;
        }
        List<ItemRequest> requestList = requestRepository.findByNotRequesterId(requesterId,
                PageRequest.of(from, size)).getContent();
        if (requestList.isEmpty()) {
            return requestDtoWithListItem;
        }
        System.out.println("Вот тут");
        List<Integer> requestIdList = new ArrayList<>();

        for (ItemRequest request : requestList) {
            requestIdList.add(request.getId());
        }
        List<Item> itemList = itemRepository.findByRequestIn(requestIdList);
        if (!itemList.isEmpty()) {
            HashMap<Integer, List<ItemDtoForRequest>> itemDtoForRequestMap = new HashMap<>();
            for (Item item : itemList) {
                if (itemDtoForRequestMap.containsKey(item.getRequest())) {
                    itemDtoForRequestMap.get(item.getRequest()).add(ItemMapper.itemDtoForRequest(item));
                } else {
                    List<ItemDtoForRequest> tempList = new ArrayList<>();
                    tempList.add(ItemMapper.itemDtoForRequest(item));
                    itemDtoForRequestMap.put(item.getRequest(), tempList);
                }
            }
            for (ItemRequest itemRequest : requestList) {
                requestDtoWithListItem.add(RequestMapper.toRequestDtoWithListItem(itemRequest,
                        itemDtoForRequestMap.get(itemRequest.getId())));
            }
            return requestDtoWithListItem;
        } else {
            for (ItemRequest itemRequest : requestList) {
                requestDtoWithListItem.add(RequestMapper.toRequestDtoWithListItem(itemRequest,
                        new ArrayList<>()));
            }
            return requestDtoWithListItem;
        }
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
