package com.weatherapp.controller;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.weatherapp.dto.WeatherResponseDto;
import com.weatherapp.entity.WeatherRequest;
import com.weatherapp.service.WeatherService;
import com.weatherapp.utils.ValidationUtil;

@WebMvcTest(WeatherController.class)
class WeatherControllerIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private WeatherService weatherService;

	@MockBean
	private ValidationUtil validationUtil;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void testGetWeatherSuccess() throws Exception {

		WeatherResponseDto resp = new WeatherResponseDto("12345", "Sunny", 17.5);
//		Mockito.doNothing().when(validationUtil).validatePostalCode("12345");
		Mockito.when(weatherService.fetchWeather(eq("12345"), eq("john_doe"))).thenReturn(resp);

		mockMvc.perform(get("/api/weather/12345/john_doe")).andExpect(status().isOk())
				.andExpect(content().string(containsString("Sunny")));
	}

	@Test
	void testGetWeatherInvalidPostalCode() throws Exception {

		mockMvc.perform(get("/api/weather/abc/john_doe")).andExpect(status().isBadRequest());
	}

	@Test
	void testGetWeatherServerError() throws Exception {

		Mockito.when(weatherService.fetchWeather(eq("12345"), eq("john_doe")))
				.thenThrow(new RuntimeException("Unexpected error"));

		mockMvc.perform(get("/api/weather/12345/john_doe")).andExpect(status().isInternalServerError())
				.andExpect(content().json("{\"message\":\"Unexpected error\"}"));
	}

	@Test
	void testGetHistorySuccess() throws Exception {
		List<WeatherRequest> list = getWeatherRequestList();

		Mockito.when(weatherService.getHistory(eq("12345"), eq("john_doe"))).thenReturn(list);

		mockMvc.perform(get("/api/weather/history").param("postalCode", "12345").param("user", "john_doe"))
				.andExpect(status().isOk());
	}

	@Test
	void testGetHistoryByPostalCodeSuccess() throws Exception {

		List<WeatherRequest> list = getWeatherRequestList();

//		Mockito.doNothing().when(validationUtil).validatePostalCode("12345");
		Mockito.when(weatherService.getHistoryByPostalCode(eq("12345"))).thenReturn(list);

		mockMvc.perform(get("/api/weather/history/postalcode").param("postalCode", "12345")).andExpect(status().isOk());
	}

	@Test
	void testGetHistoryByUserSuccess() throws Exception {

		List<WeatherRequest> list = getWeatherRequestList();

		Mockito.when(weatherService.getHistoryByUser(eq("john_doe"))).thenReturn(list);

		mockMvc.perform(get("/api/weather/history/user").param("user", "john_doe")).andExpect(status().isOk())
				.andExpect(content().string(containsString("[]")));
	}

	private List<WeatherRequest> getWeatherRequestList() {
		WeatherRequest req = new WeatherRequest();
		req.setPostalCode("12345");
		req.setTemperature(17.5);
		req.setUser("john_doe");
		req.setWeatherDescription("sunny");
		List<WeatherRequest> list = new ArrayList<>();
		return list;
	}

	@Test
	void testGetHistoryByUserServerError() throws Exception {
		Mockito.when(weatherService.getHistoryByUser(eq("john_doe")))
				.thenThrow(new RuntimeException("Unexpected error"));

		mockMvc.perform(get("/api/weather/history/user").param("user", "john_doe"))
				.andExpect(status().isInternalServerError())
				.andExpect(content().json("{\"message\":\"Unexpected error\"}"));
	}
}
