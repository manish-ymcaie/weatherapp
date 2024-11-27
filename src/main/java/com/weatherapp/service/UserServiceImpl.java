package com.weatherapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.weatherapp.dto.UserDto;
import com.weatherapp.entity.User;
import com.weatherapp.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository repository;

	@Override
	public UserDto activateUser(UserDto user) {
		
		if (user.getName() == null || user.getName().isEmpty()) {
			throw new IllegalArgumentException("Invalid User.");
		}

		try {
			User userEntity = new User();
			userEntity.setName(user.getName());
			userEntity.setEmail(user.getEmail());
			userEntity.setActive(true);

			repository.save(userEntity);
			user.setId(userEntity.getId());
			return user;
		} catch (Exception e) {
			throw new RuntimeException("Failed to save user.");
		}

	}

	@Override
	public void deactivateUser(String name) {
		try {
			int countDeactivated = repository.deactivateUser(name);
			if(countDeactivated == 0) {
				throw new Exception("Failed to deactivate user.");
			}

		} catch (Exception e) {
			throw new RuntimeException("Failed to deactivate user.");

		}
	}

	@Override
	public User findByUserName(String name) {
		return repository.findByUserName(name);

	}
}
