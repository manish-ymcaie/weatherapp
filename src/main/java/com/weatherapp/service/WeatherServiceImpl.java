package com.weatherapp.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.weatherapp.dto.WeatherResponseDto;
import com.weatherapp.entity.User;
import com.weatherapp.entity.WeatherRequest;
import com.weatherapp.repository.WeatherRequestRepository;

@Service
public class WeatherServiceImpl implements WeatherService {

	@Value("${api.key}")
	public String apiKey;

	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	private WeatherRequestRepository repository;

	@Autowired
	private UserService userService;

	@Override
	public WeatherResponseDto fetchWeather(String postalCode, String user) throws Exception {
		isUserActive(user);

		String url = "https://api.openweathermap.org/data/2.5/weather?zip=" + postalCode + ",us&appid=" + apiKey
				+ "&units=metric";

		try {

			ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
			Map<String, Object> body = response.getBody();

			String weatherDescription = ((Map<String, String>) ((List<?>) body.get("weather")).get(0))
					.get("description");
			double temperature = (double) ((Map<String, Object>) body.get("main")).get("temp");

			WeatherRequest request = new WeatherRequest();
			request.setPostalCode(postalCode);
			request.setUser(user);
			request.setWeatherDescription(weatherDescription);
			request.setTemperature(temperature);
			request.setTimestamp(LocalDateTime.now());
			repository.save(request);

			return new WeatherResponseDto(postalCode, weatherDescription, temperature);
		} catch (Exception e) {
			throw new RuntimeException("Failed to fetch weather data.");
		}
	}

	@Override
	public List<WeatherRequest> getHistory(String postalCode, String user) {
		isUserActive(user);

		return repository.findByPostalCodeOrUser(postalCode, user);
	}

	private void isUserActive(String user) {
		User userObj = null;
		try {
			userObj = userService.findByUserName(user);

		} catch (Exception e) {
			throw new IllegalArgumentException("Failed to fetch user data.");

		}

		if (userObj == null || !userObj.isActive()) {
			throw new IllegalArgumentException("InValid User.");
		}
	}

	@Override
	public List<WeatherRequest> getHistoryByPostalCode(String postalCode) {
		List<WeatherRequest> list = repository.findByPostalCode(postalCode);
		List<WeatherRequest> finallist = list.stream().filter(e-> {
			String user = e.getUser();
			
			User userObj = null;
			try {
				userObj = userService.findByUserName(user);

			} catch (Exception e1) {
				return false;
			}

			if (userObj == null || !userObj.isActive()) {
				return false;

			}
			return true;
		}).collect(Collectors.toList());
		
		return finallist;
	}

	@Override
	public List<WeatherRequest> getHistoryByUser(String user) {
		isUserActive(user);

		return repository.findByUser(user);
	}
}
