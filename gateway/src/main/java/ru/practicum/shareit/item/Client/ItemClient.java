package ru.practicum.shareit.item.Client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.comments.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Map;

@Service
public class ItemClient extends BaseClient {
    private static final String API_PREFIX = "/items";

    @Autowired
    public ItemClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    public ResponseEntity<Object> createItem(int userId, ItemDto itemDto) {
        return post("",userId, itemDto);
    }

    public ResponseEntity<Object> updateItem(int itemId, int userId, ItemDto itemDto) {
        return patch("/" + itemId, userId, itemDto);
    }

    public ResponseEntity<Object> getItem(int itemId, int userId) {
        return get("/" + itemId, userId);
    }

    public ResponseEntity<Object> getAllItemByUser(long id, int from, int size) {
        Map<String, Object> parameters = Map.of(
                "from", from,
                "size", size
        );
        return get("", id, parameters);
    }

    public ResponseEntity<Object> searchItem(@RequestParam String text, long userId) {
        Map<String, Object> parameters = Map.of(
                "text", text
        );
        return get("/search", userId, parameters);
    }

    public ResponseEntity<Object> createComment(@RequestHeader("X-Sharer-User-Id") int userId,
                                    @PathVariable int itemId, @RequestBody CommentDto comment) {
        return post("/" + itemId, userId, comment);
    }
}
