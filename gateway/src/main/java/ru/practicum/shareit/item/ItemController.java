package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.comments.dto.CommentDto;
import ru.practicum.shareit.item.Client.ItemClient;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.Valid;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemClient service;

    @PostMapping
    public ResponseEntity<Object> createItem(@RequestHeader("X-Sharer-User-Id") int userId, @Valid @RequestBody ItemDto itemDto) {
        return service.createItem(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateItem(@PathVariable int itemId, @RequestHeader("X-Sharer-User-Id") int userId, @RequestBody ItemDto itemDto) {
        return service.updateItem(itemId, userId, itemDto);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItem(@PathVariable int itemId, @RequestHeader("X-Sharer-User-Id") int userId) {
        return service.getItem(itemId, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getAllItemByUser(@RequestHeader("X-Sharer-User-Id") long id,
                                                    @RequestParam(name = "from",
                                                                  defaultValue = "0") int from,
                                                    @RequestParam(name = "size",
                                                                  defaultValue = "20") int size) {
        return service.getAllItemByUser(id, from, size);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchItem(@RequestHeader("X-Sharer-User-Id") int userId,
                                             @RequestParam String text) {
        return service.searchItem(text, userId);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> createComment(@RequestHeader("X-Sharer-User-Id") int userId,
                                    @PathVariable int itemId, @RequestBody CommentDto comment) {
        return service.createComment(userId, itemId, comment);
    }
}
