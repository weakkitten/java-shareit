package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.Client.RequestClient;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping(path = "/requests")
@Slf4j
@RequiredArgsConstructor
@Validated
public class ItemRequestController {
    @Autowired
    private final RequestClient service;

    @PostMapping
    public ResponseEntity<Object> createRequest(@RequestHeader("X-Sharer-User-Id") int userId,
                                                @Valid @RequestBody ItemRequestDto dto) {
        return service.createRequest(userId, dto);
    }

    @GetMapping
    public ResponseEntity<Object> getAllUserRequest(@RequestHeader("X-Sharer-User-Id") int userId) {
        return service.getAllUserRequest(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllOtherUsersRequest(@RequestHeader("X-Sharer-User-Id") int userId,
           @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") int from,
           @Positive @RequestParam(name = "size", defaultValue = "20") int size) {
        return service.getAllOtherUsersRequest(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getRequestById(@RequestHeader("X-Sharer-User-Id") int userId,
                                                 @PathVariable int requestId) {
        return service.getRequestById(userId, requestId);
    }
}
