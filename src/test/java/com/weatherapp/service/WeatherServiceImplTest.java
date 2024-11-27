package com.weatherapp.service;

import com.weatherapp.dto.WeatherResponseDto;
import com.weatherapp.entity.User;
import com.weatherapp.entity.WeatherRequest;
import com.weatherapp.repository.WeatherRequestRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class WeatherServiceImplTest {

    @InjectMocks
    private WeatherServiceImpl weatherService;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private WeatherRequestRepository repository;

    @Mock
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        weatherService.apiKey = "testApiKey"; // Injecting test API key
    }

    @Test
    void testFetchWeather_Success() throws Exception {
        // Arrange
        String postalCode = "12345";
        String user = "john_doe";
        Map<String, Object> weatherData = Map.of(
                "weather", List.of(Map.of("description", "clear sky")),
                "main", Map.of("temp", 25.0)
        );
        User userobj = getSampleUser(user);
        when(userService.findByUserName(eq(user))).thenReturn(userobj);
        when(restTemplate.getForEntity(any(String.class), eq(Map.class)))
                .thenReturn(ResponseEntity.ok(weatherData));

        ArgumentCaptor<WeatherRequest> captor = ArgumentCaptor.forClass(WeatherRequest.class);

        // Act
        WeatherResponseDto response = weatherService.fetchWeather(postalCode, user);

        // Assert
        assertNotNull(response);
        assertEquals(postalCode, response.getPostalCode());
        assertEquals("clear sky", response.getWeatherDescription());
        assertEquals(25.0, response.getTemperature());

        verify(repository).save(captor.capture());
        WeatherRequest savedRequest = captor.getValue();
        assertEquals(postalCode, savedRequest.getPostalCode());
        assertEquals(user, savedRequest.getUser());
    }

	private User getSampleUser(String user) {
		User userobj = new User();
        userobj.setId(1L);
        userobj.setActive(true);
        userobj.setName(user);
        userobj.setEmail("a@a.com");
		return userobj;
	}

    @Test
    void testFetchWeather_InvalidUser() {
        // Arrange
        String postalCode = "12345";
        String user = "invalid_user";
        when(userService.findByUserName(eq(user))).thenReturn(null);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                weatherService.fetchWeather(postalCode, user));
        assertEquals("InValid User.", exception.getMessage());
    }

    @Test
    void testFetchWeather_RestTemplateError() {
        // Arrange
        String postalCode = "12345";
        String user = "john_doe";
        when(userService.findByUserName(eq(user))).thenReturn(getSampleUser(user));
        when(restTemplate.getForEntity(any(String.class), eq(Map.class)))
                .thenThrow(new RuntimeException("API error"));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                weatherService.fetchWeather(postalCode, user));
        assertEquals("Failed to fetch weather data.API error", exception.getMessage());
    }

    @Test
    void testGetHistory_Success() {
        // Arrange
        String postalCode = "12345";
        String user = "john_doe";
        when(userService.findByUserName(eq(user))).thenReturn(getSampleUser(user));
        when(repository.findByPostalCodeOrUser(eq(postalCode), eq(user)))
                .thenReturn(List.of(new WeatherRequest(1L, postalCode, user, "clear sky", 25.0, LocalDateTime.now())));

        // Act
        List<WeatherRequest> history = weatherService.getHistory(postalCode, user);

        // Assert
        assertNotNull(history);
        assertEquals(1, history.size());
        assertEquals(postalCode, history.get(0).getPostalCode());
        assertEquals(user, history.get(0).getUser());
    }

    @Test
    void testGetHistory_InvalidUser() {
        // Arrange
        String postalCode = "12345";
        String user = "invalid_user";
        when(userService.findByUserName(eq(user))).thenReturn(null);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                weatherService.getHistory(postalCode, user));
        assertEquals("InValid User.", exception.getMessage());
    }

    @Test
    void testGetHistoryByPostalCode_Success() {
        // Arrange
        String postalCode = "12345";
        String user = "john_doe";
        WeatherRequest request = new WeatherRequest(1L, postalCode, user, "clear sky", 25.0, LocalDateTime.now());
        when(repository.findByPostalCode(eq(postalCode))).thenReturn(List.of(request));
        when(userService.findByUserName(eq(user))).thenReturn(getSampleUser(user));

        // Act
        List<WeatherRequest> history = weatherService.getHistoryByPostalCode(postalCode);

        // Assert
        assertNotNull(history);
        assertEquals(1, history.size());
        assertEquals(postalCode, history.get(0).getPostalCode());
    }

    @Test
    void testGetHistoryByUser_Success() {
        // Arrange
        String user = "john_doe";
        when(userService.findByUserName(eq(user))).thenReturn(getSampleUser(user));
        when(repository.findByUser(eq(user)))
                .thenReturn(List.of(new WeatherRequest(1L, "12345", user, "clear sky", 25.0, LocalDateTime.now())));

        // Act
        List<WeatherRequest> history = weatherService.getHistoryByUser(user);

        // Assert
        assertNotNull(history);
        assertEquals(1, history.size());
        assertEquals(user, history.get(0).getUser());
    }
}
