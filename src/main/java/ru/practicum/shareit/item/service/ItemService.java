package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDtoItem;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.comments.model.Comment;
import ru.practicum.shareit.comments.repository.CommentRepository;
import ru.practicum.shareit.error.exception.BadRequestException;
import ru.practicum.shareit.error.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoGet;
import ru.practicum.shareit.item.dto.ItemDtoGetBooking;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.utility.Status;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final BookingRepository bookingRepository;

    public ItemDtoGet createItem(int userId, ItemDto itemDto) {
        Item item = ItemMapper.toItem(userId, itemDto);
        if (userRepository.findById(userId).isEmpty()) {
            throw new NotFoundException("Нет такого пользователя");
        }
        itemRepository.save(item);
        return ItemMapper.toItemDtoGet(itemRepository.findByNameAndOwner(item.getName(), item.getOwner()));
    }

    public ItemDtoGet updateItem(int itemId, int userId, ItemDto itemDto) {
        Item itemRep = itemRepository.findById(itemId).get();

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
        itemRepository.save(itemRep);
        return ItemMapper.toItemDtoGet(itemRepository.findById(itemId).get());
    }

    public ItemDtoGetBooking getItem(int itemId, int userId) {
        if (itemRepository.findById(itemId).isEmpty()) {
            throw new NotFoundException("Нет предмета с таким id");
        }
        Item item = itemRepository.findById(itemId).get();
        BookingDtoItem lastBooking = null;
        BookingDtoItem nextBooking = null;

        if (item.getOwner() != userId) {
            return ItemMapper.itemDtoGetBooking(item, lastBooking, nextBooking);
        }
        if (!bookingRepository.lastBooking(itemId, Status.APPROVED, LocalDateTime.now()).isEmpty()) {
            lastBooking = BookingMapper.toBookingDtoItem(bookingRepository.lastBooking(itemId,
                                                         Status.APPROVED, LocalDateTime.now()).get(0));
        }
        if (!bookingRepository.nextBooking(itemId, Status.APPROVED, LocalDateTime.now()).isEmpty()) {
            nextBooking = BookingMapper.toBookingDtoItem(bookingRepository.nextBooking(itemId,
                                                         Status.APPROVED, LocalDateTime.now()).get(0));
        }
        return ItemMapper.itemDtoGetBooking(item, lastBooking, nextBooking);
    }

    public List<ItemDtoGetBooking> getAllUserItems(int userId) {
        List<ItemDtoGetBooking> itemDtoList = new ArrayList<>();

        for (Item item : itemRepository.findByOwner(userId)) {
            BookingDtoItem lastBooking = null;
            BookingDtoItem nextBooking = null;
            if (!bookingRepository.lastBooking(item.getId(), Status.APPROVED, LocalDateTime.now()).isEmpty()) {
                lastBooking = BookingMapper.toBookingDtoItem(bookingRepository.lastBooking(item.getId(),
                        Status.APPROVED, LocalDateTime.now()).get(0));
            }
            if (!bookingRepository.nextBooking(item.getId(), Status.APPROVED, LocalDateTime.now()).isEmpty()) {
                nextBooking = BookingMapper.toBookingDtoItem(bookingRepository.nextBooking(item.getId(),
                        Status.APPROVED, LocalDateTime.now()).get(0));
            }
            itemDtoList.add(ItemMapper.itemDtoGetBooking(item, lastBooking, nextBooking));
        }
        return itemDtoList;
    }

    public List<ItemDtoGet> searchItem(String text) {
        List<ItemDtoGet> itemDtoList = new ArrayList<>();
        if (!text.isBlank()) {
            for (Item item : itemRepository.search(text)) {
                itemDtoList.add(ItemMapper.toItemDtoGet(item));
            }
        }
        return itemDtoList;
    }

    public Comment createComment(int userId, int itemId, Comment comment) {
        Booking booking = bookingRepository.findByBookerIdAndItemId(userId, itemId);

        if (booking == null) {
            throw new BadRequestException("Отказано в доступе: пользователь с таким id" +
                                          " не брал вещь в аренду");
        }
        commentRepository.save(comment);
        return commentRepository.findById(comment.getId()).get();
    }

    public List<Comment> getCommentByItem(int itemId) {
        return commentRepository.findByItemId(itemId);
    }

    public List<Comment> getAllCommentOnUsersItems(int userId) {
        List<Item> itemList = itemRepository.findByOwner(userId);
        List<Comment> commentsList = new ArrayList<>();

        for (Item item : itemList) {
            if (commentRepository.findByItemId(item.getId()) != null) {
                commentsList.addAll(commentRepository.findByItemId(item.getId()));
            }
        }
        return commentsList;
    }
}
