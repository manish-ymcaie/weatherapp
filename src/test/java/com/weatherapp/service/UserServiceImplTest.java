package com.weatherapp.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.weatherapp.dto.UserDto;
import com.weatherapp.entity.User;
import com.weatherapp.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testActivateUser_Success() {
        // Arrange
        UserDto userDto = new UserDto(1L, "John", "john@example.com");

        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setName("John");
        savedUser.setEmail("john@example.com");
        savedUser.setActive(true);

        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // Act
        UserDto result = userService.activateUser(userDto);

        // Assert
        assertNotNull(result);
        assertEquals("John", result.getName());
        assertEquals("john@example.com", result.getEmail());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testActivateUser_InvalidUser() {
        // Arrange
        UserDto userDto = new UserDto(1L, null,"john@example.com");

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.activateUser(userDto);
        });

        assertEquals("Invalid User.", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testActivateUser_SaveFails() {
        // Arrange
        UserDto userDto = new UserDto(1L, "John", "john@example.com");



        when(userRepository.save(any(User.class))).thenThrow(new RuntimeException("Database error"));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.activateUser(userDto);
        });

        assertEquals("Failed to save user.Database error", exception.getMessage());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testDeactivateUser_Success() {
        // Arrange
        String userName = "John";

        when(userRepository.deactivateUser(any(String.class))).thenReturn(1);

        // Act
        userService.deactivateUser(userName);

        // Assert
        verify(userRepository, times(1)).deactivateUser(userName);
    }

    @Test
    void testDeactivateUser_Failure() {
        // Arrange
        String userName = "John";
        doThrow(new RuntimeException("Database error")).when(userRepository).deactivateUser(userName);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.deactivateUser(userName);
        });

        assertEquals("Failed to deactivate user.Database error", exception.getMessage());
        verify(userRepository, times(1)).deactivateUser(userName);
    }

    @Test
    void testFindByUserName_Success() {
        // Arrange
        String userName = "John";
        User user = new User();
        user.setId(1L);
        user.setName(userName);
        user.setActive(true);

        when(userRepository.findByUserName(userName)).thenReturn(user);

        // Act
        User result = userService.findByUserName(userName);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(userName, result.getName());
        verify(userRepository, times(1)).findByUserName(userName);
    }

    @Test
    void testFindByUserName_UserNotFound() {
        // Arrange
        String userName = "John";

        when(userRepository.findByUserName(userName)).thenReturn(null);

        // Act
        User result = userService.findByUserName(userName);

        // Assert
        assertNull(result);
        verify(userRepository, times(1)).findByUserName(userName);
    }
}
