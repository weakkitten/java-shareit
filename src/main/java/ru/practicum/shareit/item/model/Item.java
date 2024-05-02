package ru.practicum.shareit.item.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Positive;

@Data
@Builder
public class Item {
    @Positive
    private int id;
    private String name;
    private String description;
    private boolean available;
    @Positive
    private int owner;
    @Positive
    private int request;
}
