package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import ru.practicum.shareit.error.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoGet;
import ru.practicum.shareit.request.dto.RequestDtoWithListItem;
import ru.practicum.shareit.request.dto.RequestMapper;
import ru.practicum.shareit.request.repository.RequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class RequestServiceTest {
    @InjectMocks
    private RequestService service;
    @Mock
    private RequestRepository requestRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ItemRepository itemRepository;
    ItemRequestDto itemRequestDto = ItemRequestDto.builder()
            .description("Описание запроса")
            .created(LocalDateTime.of(2024, 11,11,11,11))
            .build();

    User user = User.builder()
            .id(2)
            .name("Имя")
            .email("Почта")
            .build();

    Item item = Item.builder()
            .id(0)
            .name("Имя")
            .description("Описание")
            .available(true)
            .owner(1)
            .request(1)
            .build();

    ItemRequest request = RequestMapper.toItemRequest(itemRequestDto, 1);
    ItemRequestDtoGet itemRequestDtoGet = RequestMapper.toItemRequestDtoGet(request);

    @Test
    void createRequestReturnBadRequest() {
        assertThrows(NotFoundException.class,
                () -> service.createRequest(itemRequestDto, Mockito.anyInt()));
    }

    @Test
    void createRequest() {
        Mockito.when(userRepository.findById(Mockito.anyInt())).thenReturn(Optional.ofNullable(user));
        Mockito
                .when(requestRepository.findByDescriptionAndRequesterId(Mockito.anyString(),
                        Mockito.anyInt()))
                .thenReturn(request);
        assertEquals(service.createRequest(itemRequestDto, 2), itemRequestDtoGet);
    }

    @Test
    void getAllUserRequestReturnNotFound() {
        List<RequestDtoWithListItem> requestDtoWithListItemList = new ArrayList<>();
        List<Integer> requestIdList = new ArrayList<>();
        List<ItemRequest> itemRequestList = new ArrayList<>();
        itemRequestList.add(request);
        Mockito.when(requestRepository.findByRequesterIdOrderByCreatedDesc(Mockito.anyInt())).thenReturn(itemRequestList);
        assertThrows(NotFoundException.class, () -> service.getAllUserRequest(1));
    }

    @Test
    void getAllUserRequest() {
        List<RequestDtoWithListItem> requestDtoWithListItemList = new ArrayList<>();
        List<ItemRequest> itemRequestList = new ArrayList<>();
        requestDtoWithListItemList.add(RequestMapper.toRequestDtoWithListItem(request, null));
        itemRequestList.add(request);

        Mockito.when(requestRepository.findByRequesterIdOrderByCreatedDesc(Mockito.anyInt())).thenReturn(itemRequestList);
        Mockito.when(userRepository.findById(Mockito.anyInt())).thenReturn(Optional.ofNullable(user));
        Mockito.when(itemRepository.findByRequestIn(Mockito.anyList())).thenReturn(List.of(item));
        assertEquals(service.getAllUserRequest(1), requestDtoWithListItemList);
    }

    @Test
    void getAllOtherUsersRequest() {//Не понимаю как писать тесты для Page
    }

    @Test
    void getRequestReturnNotFoundRequesterId() {
        assertThrows(NotFoundException.class, () -> service.getRequest(1, 1));
    }

    @Test
    void getRequestReturnNotFoundRequestId() {
        Mockito.when(userRepository.findById(Mockito.anyInt())).thenReturn(Optional.ofNullable(user));
        assertThrows(NotFoundException.class, () -> service.getRequest(1, 1));
    }

    @Test
    void getRequestReturn() {
        RequestDtoWithListItem requestDtoWithListItem = RequestMapper.
                toRequestDtoWithListItem(request, List.of(ItemMapper.itemDtoForRequest(item)));

        Mockito.when(userRepository.findById(Mockito.anyInt())).thenReturn(Optional.ofNullable(user));
        Mockito.when(requestRepository.findById(Mockito.anyInt())).thenReturn(Optional.ofNullable(request));
        Mockito.when(itemRepository.findByRequest(Mockito.anyInt())).thenReturn(List.of(item));

        assertEquals(service.getRequest(1, 1), requestDtoWithListItem);
    }
}