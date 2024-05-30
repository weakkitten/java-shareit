package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.utility.Status;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

@Data
@Builder
public class BookingDto {
    private int itemId;
    @NotNull
    private LocalDateTime start;
    @Future
    @NotNull
    private LocalDateTime end;
}
