package ru.practicum.shareit.user.model;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import javax.persistence.*;
import javax.validation.constraints.*;

@Data
@Builder
@Entity
@Table(name = "users", schema = "public")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected int id;
    protected String name;
    @NonNull
    @Email
    @NotBlank
    protected String email;
}
