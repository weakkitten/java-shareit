package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDtoItem;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.comments.dto.CommentDto;
import ru.practicum.shareit.comments.dto.CommentGet;
import ru.practicum.shareit.comments.dto.CommentMapper;
import ru.practicum.shareit.comments.model.Comment;
import ru.practicum.shareit.comments.repository.CommentRepository;
import ru.practicum.shareit.error.exception.BadRequestException;
import ru.practicum.shareit.error.exception.CommentTextIsEmpty;
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

    @Transactional
    public ItemDtoGet createItem(int userId, ItemDto itemDto) {
        Item item = ItemMapper.toItem(userId, itemDto);
        if (userRepository.findById(userId).isEmpty()) {
            throw new NotFoundException("Нет такого пользователя");
        }
        itemRepository.save(item);
        return ItemMapper.toItemDtoGet(itemRepository.findByNameAndOwner(item.getName(), item.getOwner()));
    }

    @Transactional
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
        List<Comment> commentList = commentRepository.findByItemId(itemId);
        List<CommentGet> commentGetList = new ArrayList<>();
        for (Comment comment : commentList) {
            commentGetList.add(CommentMapper.toCommentGet(comment,
                    userRepository.findById(comment.getAuthorId()).get().getName()));
        }
        System.out.println("Как работает - " + userId);
        System.out.println(bookingRepository.nextBooking(itemId, Status.APPROVED, LocalDateTime.now()));

        if (item.getOwner() != userId) {
            return ItemMapper.itemDtoGetBooking(item, lastBooking, nextBooking, commentGetList);
        }
        if (!bookingRepository.lastBooking(itemId, Status.APPROVED, LocalDateTime.now()).isEmpty()) {
            lastBooking = BookingMapper.toBookingDtoItem(bookingRepository.lastBooking(itemId,
                                                         Status.APPROVED, LocalDateTime.now()).get(0));
        }
        if (!bookingRepository.nextBooking(itemId, Status.APPROVED, LocalDateTime.now()).isEmpty()) {
            nextBooking = BookingMapper.toBookingDtoItem(bookingRepository.nextBooking(itemId,
                                                         Status.APPROVED, LocalDateTime.now()).get(0));
        }
        System.out.println(lastBooking);
        System.out.println(nextBooking);
        return ItemMapper.itemDtoGetBooking(item, lastBooking, nextBooking, commentGetList);
    }

    public List<ItemDtoGetBooking> getAllUserItems(int userId) {
        List<ItemDtoGetBooking> itemDtoList = new ArrayList<>();

        for (Item item : itemRepository.findByOwner(userId)) {
            BookingDtoItem lastBooking = null;
            BookingDtoItem nextBooking = null;
            List<Comment> commentList = commentRepository.findByItemId(item.getId());
            List<CommentGet> commentGetList = new ArrayList<>();
            for (Comment comment : commentList) {
                commentGetList.add(CommentMapper.toCommentGet(comment,
                        userRepository.findById(comment.getAuthorId()).get().getName()));
            }
            if (!bookingRepository.lastBooking(item.getId(), Status.APPROVED, LocalDateTime.now()).isEmpty()) {
                lastBooking = BookingMapper.toBookingDtoItem(bookingRepository.lastBooking(item.getId(),
                        Status.APPROVED, LocalDateTime.now()).get(0));
            }
            if (!bookingRepository.nextBooking(item.getId(), Status.APPROVED, LocalDateTime.now()).isEmpty()) {
                nextBooking = BookingMapper.toBookingDtoItem(bookingRepository.nextBooking(item.getId(),
                        Status.APPROVED, LocalDateTime.now()).get(0));
            }
            itemDtoList.add(ItemMapper.itemDtoGetBooking(item, lastBooking, nextBooking, commentGetList));
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

    @Transactional
    public CommentGet createComment(int userId, int itemId, CommentDto comment) {
        List<Booking> booking = bookingRepository.findByBookerIdAndItemIdAndStatusAndEndLessThan(userId, itemId,
                                                                          Status.APPROVED, LocalDateTime.now());

        if (booking.isEmpty()) {
            throw new BadRequestException("Отказано в доступе: пользователь с таким id" +
                                          " не брал вещь в аренду");
        }
        if (comment.getText().isEmpty()) {
            throw new CommentTextIsEmpty("Текст отсутствует");
        }
        commentRepository.save(CommentMapper.toComment(comment, userId, itemId));
        return CommentMapper.toCommentGet(commentRepository.findByItemIdAndAuthorIdAndText(itemId, userId, comment.getText()),
                userRepository.findById(userId).get().getName());
    }
}
