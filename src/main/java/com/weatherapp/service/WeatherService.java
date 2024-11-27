package com.weatherapp.service;

import java.util.List;

import com.weatherapp.dto.WeatherResponseDto;
import com.weatherapp.entity.WeatherRequest;

public interface WeatherService {
    WeatherResponseDto fetchWeather(String postalCode, String user) throws Exception;
    List<WeatherRequest> getHistory(String postalCode, String user);
    List<WeatherRequest> getHistoryByPostalCode(String postalCode);
    List<WeatherRequest> getHistoryByUser(String user);
    
    
}
