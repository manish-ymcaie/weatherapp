package com.weatherapp.controller;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.weatherapp.dto.UserDto;
import com.weatherapp.service.UserService;

@WebMvcTest(UserController.class)
class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testAddUserSuccess() throws Exception {
        UserDto userDto = new UserDto(1L, "John", "john.doe@example.com");
        Mockito.when(userService.activateUser(any(UserDto.class))).thenReturn(userDto);

        mockMvc.perform(post("/api/user/activate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("{\"id\":1,\"name\":\"John\",\"email\":\"john.doe@example.com\"}")));
    }

    @Test
    void testAddUserBadRequest() throws Exception {
        UserDto userDto = new UserDto(1L, "", "john.doe@example.com"); // Invalid user data
        Mockito.when(userService.activateUser(any(UserDto.class)))
                .thenThrow(new IllegalArgumentException("Invalid user details"));

        mockMvc.perform(post("/api/user/activate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"message\":\"Invalid user details\"}"));
    }

    @Test
    void testAddUserServerError() throws Exception {
        UserDto userDto = new UserDto(1L, null, "john.doe@example.com");
        Mockito.when(userService.activateUser(any(UserDto.class)))
                .thenThrow(new RuntimeException("Unexpected error"));

        mockMvc.perform(post("/api/user/activate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().json("{\"message\":\"Unexpected error\"}"));
    }

    @Test
    void testDeactivateUserSuccess() throws Exception {
        Mockito.doNothing().when(userService).deactivateUser("John");

        mockMvc.perform(get("/api/user/deactivate/John"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Success")));
    }

    @Test
    void testDeactivateUserServerError() throws Exception {
        Mockito.doThrow(new RuntimeException("User not found")).when(userService).deactivateUser("John");

        mockMvc.perform(get("/api/user/deactivate/John"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(containsString("User not found")));
    }
}
