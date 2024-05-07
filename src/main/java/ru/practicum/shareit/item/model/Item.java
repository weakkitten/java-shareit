package ru.practicum.shareit.item.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Positive;

@Data
@Builder
public class Item {
    private int id;
    private String name;
    private String description;
    private boolean available;
    private int owner;
    private int request;
}
