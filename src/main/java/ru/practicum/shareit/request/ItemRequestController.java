package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDtoGet;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.RequestDtoWithListItem;
import ru.practicum.shareit.request.service.RequestService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/requests")
@Slf4j
@RequiredArgsConstructor
public class ItemRequestController {
    private final RequestService service;

    @PostMapping
    public ItemRequestDtoGet createRequest(@RequestHeader("X-Sharer-User-Id") int userId,
                                           @RequestBody @Valid ItemRequestDto dto) {
        return service.createRequest(dto, userId);
    }

    @GetMapping
    public List<RequestDtoWithListItem> getAllUserRequest(@RequestHeader("X-Sharer-User-Id") int userId) {
        return service.getAllUserRequest(userId);
    }

    @GetMapping("/all")
    public List<RequestDtoWithListItem> getAllOtherUsersRequest(@RequestHeader("X-Sharer-User-Id") int userId,
                                          @RequestParam(name = "from",
                                                        defaultValue = "0") int from,
                                          @RequestParam(name = "size",
                                                        defaultValue = "20") int size) {
        return service.getAllOtherUsersRequest(from, size, userId);
    }

    @GetMapping("/{requestId}")
    public RequestDtoWithListItem getRequestById(@RequestHeader("X-Sharer-User-Id") int userId,
                                                 @PathVariable int requestId) {
        return service.getRequest(userId, requestId);
    }
}
