package com.weatherapp.dto;

public class WeatherResponseDto {
	
	String postalCode;
	String weatherDescription;
	double temperature;
	
	public WeatherResponseDto(String postalCode, String weatherDescription, double temperature){
		this.postalCode=postalCode;
		this.weatherDescription = weatherDescription;
		this.temperature= temperature;
		
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public String getWeatherDescription() {
		return weatherDescription;
	}

	public void setWeatherDescription(String weatherDescription) {
		this.weatherDescription = weatherDescription;
	}

	public double getTemperature() {
		return temperature;
	}

	public void setTemperature(double temperature) {
		this.temperature = temperature;
	}
	
	
	

}
