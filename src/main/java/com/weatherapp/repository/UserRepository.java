package com.weatherapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.weatherapp.entity.User;

import jakarta.transaction.Transactional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	
	@Transactional
	@Modifying
	@Query("update User t set t.active = false where t.name =:name")
	public int deactivateUser(@Param("name") String name);
	
	
	@Query("select t from User t where t.name =:name")
	public User findByUserName(@Param("name") String name);
	
}
