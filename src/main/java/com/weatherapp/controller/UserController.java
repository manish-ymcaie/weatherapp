package com.weatherapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.weatherapp.dto.EmptyJson;
import com.weatherapp.dto.UserDto;
import com.weatherapp.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
//import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Operation(summary = "Activate a user", 
               description = "Activate a user by providing user details.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "User activated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid user details", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal server error", 
                     content = @Content(schema = @Schema(implementation = EmptyJson.class)))
    })
    @PostMapping("/activate")
    public ResponseEntity<Object> addUser(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "User details for activation", required = true, 
                         content = @Content(schema = @Schema(implementation = UserDto.class))) 
            @RequestBody UserDto user) {
        try {
            return ResponseEntity.ok(userService.activateUser(user));
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

    @Operation(summary = "Deactivate a user", 
               description = "Deactivate a user by providing their user name.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "User deactivated successfully"),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @GetMapping("/deactivate/{name}")
    public ResponseEntity<String> deActivateUser(
            @Parameter(description = "Name of the user to deactivate", 
                                                                example = "John") 
            @PathVariable String name) {
        try {
            userService.deactivateUser(name);
            return ResponseEntity.ok("Success");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
