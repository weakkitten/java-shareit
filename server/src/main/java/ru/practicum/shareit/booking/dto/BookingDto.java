package ru.practicum.shareit.booking.dto;

import jakarta.validation.constraints.Future;
import lombok.Builder;
import lombok.Data;
import org.antlr.v4.runtime.misc.NotNull;
import ru.practicum.shareit.utility.Status;

import java.time.LocalDateTime;

@Data
@Builder
public class BookingDto {
    private int id;
    @Future
    @NotNull
    private LocalDateTime start;
    @Future
    @NotNull
    private LocalDateTime end;
    private int itemId;
    private int bookerId;
    private Status status;
}
