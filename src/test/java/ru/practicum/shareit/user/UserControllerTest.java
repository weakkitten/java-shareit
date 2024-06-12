package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {
    @Mock
    private UserService service;

    @InjectMocks
    private UserController controller;
    private MockMvc mockMvc;
    private final ObjectMapper mapper = new ObjectMapper();

    UserDto userDto = UserDto.builder()
            .name("name")
            .email("email@google.com")
            .build();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void createUser() throws Exception {
        String userJson = mapper.writeValueAsString(userDto);
        when(service.addUser(Mockito.any(UserDto.class))).thenReturn(userDto);
        mockMvc.perform(post("/users")
                        .content(userJson)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
/*
    @Test
    void updateUser() {
    }

    @Test
    void removeUser() {
    }

    @Test
    void getUserById() {
    }*/

    @Test
    void getAllUsers() throws Exception {
        when(service.getAllUsers()).thenReturn(List.of());
        mockMvc.perform(get("/users")).andExpect(status().isOk());
    }
}