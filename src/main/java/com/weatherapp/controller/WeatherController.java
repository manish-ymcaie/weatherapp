package com.weatherapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.weatherapp.dto.EmptyJson;
import com.weatherapp.service.WeatherService;
import com.weatherapp.utils.ValidationUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/weather")
public class WeatherController {

    @Autowired
    private WeatherService weatherService;

    @Operation(summary = "Get weather information by postal code and user", 
               description = "Fetch current weather information for the given postal code and user.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Weather information retrieved successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid postal code or User", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal server error", 
                     content = @Content(schema = @Schema(implementation = EmptyJson.class)))
    })
    @GetMapping("/{postalCode}/{user}")
    public ResponseEntity<Object> getWeather(
            @Parameter(description = "Postal code of the location", example = "12345") @PathVariable String postalCode,
            @Parameter(description = "Username of the requesting user", example = "john_doe") @PathVariable String user) {
        
        try {
        	ValidationUtil.validatePostalCode(postalCode);
            return ResponseEntity.ok(weatherService.fetchWeather(postalCode, user));
        } catch (IllegalArgumentException e) {
            EmptyJson json = new EmptyJson();
            json.setMessage(e.getMessage());
            return new ResponseEntity<>(json, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            EmptyJson json = new EmptyJson();
            json.setMessage(e.getMessage());
            return ResponseEntity.internalServerError().body(json);
        }

    }

    @Operation(summary = "Get weather history for a postal code and user", 
               description = "Retrieve historical weather data for the given postal code and user.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Weather information retrieved successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid postal code or User", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal server error", 
                     content = @Content(schema = @Schema(implementation = EmptyJson.class)))
    })
    @GetMapping("/history")
    public ResponseEntity<Object> getHistory(
            @RequestParam String postalCode, 
            @RequestParam String user) {
        try {
        	ValidationUtil.validatePostalCode(postalCode);
            return ResponseEntity.ok(weatherService.getHistory(postalCode, user));
        } catch (IllegalArgumentException e) {
            EmptyJson json = new EmptyJson();
            json.setMessage(e.getMessage());
            return new ResponseEntity<>(json, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            EmptyJson json = new EmptyJson();
            json.setMessage(e.getMessage());
            return ResponseEntity.internalServerError().body(json);
        }
    }

    @Operation(summary = "Get weather history by postal code", 
               description = "Retrieve historical weather data for a specific postal code.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Weather information retrieved successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid postal code", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal server error", 
                     content = @Content(schema = @Schema(implementation = EmptyJson.class)))
    })
    @GetMapping("/history/postalcode")
    public ResponseEntity<Object> getHistoryByPostalCode(
            @RequestParam String postalCode) {
        try {
        	ValidationUtil.validatePostalCode(postalCode);
            return ResponseEntity.ok(weatherService.getHistoryByPostalCode(postalCode));
        } catch (IllegalArgumentException e) {
            EmptyJson json = new EmptyJson();
            json.setMessage(e.getMessage());
            return new ResponseEntity<>(json, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            EmptyJson json = new EmptyJson();
            json.setMessage(e.getMessage());
            return ResponseEntity.internalServerError().body(json);
        }
    }

    @Operation(summary = "Get weather history by user", 
               description = "Retrieve historical weather data for a specific user.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Weather information retrieved successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid User", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal server error", 
                     content = @Content(schema = @Schema(implementation = EmptyJson.class)))
    })

    @GetMapping("/history/user")
    public ResponseEntity<Object> getHistoryByUser(
            @RequestParam String user) {
        try {
            return ResponseEntity.ok(weatherService.getHistoryByUser(user));
        } catch (IllegalArgumentException e) {
            EmptyJson json = new EmptyJson();
            json.setMessage(e.getMessage());
            return new ResponseEntity<>(json, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            EmptyJson json = new EmptyJson();
            json.setMessage(e.getMessage());
            return ResponseEntity.internalServerError().body(json);
        }
    }
}
