package com.weatherapp.service;

import com.weatherapp.dto.UserDto;
import com.weatherapp.entity.User;

public interface UserService {
    public UserDto activateUser(UserDto user);
    
    public void deactivateUser(String name);
    
    public User findByUserName(String name);

}
