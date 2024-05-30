package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.utility.Status;

import javax.validation.constraints.NotBlank;

@Data
@Builder
public class BookingDto {
    private int id;
    private int item_id;
    private int booker_id;
    private Status status;
}
