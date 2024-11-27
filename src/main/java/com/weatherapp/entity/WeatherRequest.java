package com.weatherapp.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "weather_request")
public class WeatherRequest {
	
	public WeatherRequest() {
		
	}
	
	public WeatherRequest(Long id, String postalCode, String user, String weatherDescription, Double temperature,
			LocalDateTime timestamp) {
		super();
		this.id = id;
		this.postalCode = postalCode;
		this.user = user;
		this.weatherDescription = weatherDescription;
		this.temperature = temperature;
		this.timestamp = timestamp;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "weather_seq")
	@SequenceGenerator(name = "weather_seq", sequenceName = "weather_seq", allocationSize = 1)
	private Long id;

	@Column(nullable = false, name = "postal_code")
	private String postalCode;
	
	@Column(nullable = false, name = "user_name")
	private String user;

	@Column(name = "weather_description")
	private String weatherDescription;
	
	@Column(name = "temperature")
	private Double temperature;

	@Column(name = "timestamp")
	private LocalDateTime timestamp;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getWeatherDescription() {
		return weatherDescription;
	}

	public void setWeatherDescription(String weatherDescription) {
		this.weatherDescription = weatherDescription;
	}

	public Double getTemperature() {
		return temperature;
	}

	public void setTemperature(Double temperature) {
		this.temperature = temperature;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}

}
