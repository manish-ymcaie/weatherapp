package com.weatherapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.weatherapp.entity.WeatherRequest;

@Repository
public interface WeatherRequestRepository extends JpaRepository<WeatherRequest, Long> {
	
	@Query("Select t from WeatherRequest t where t.postalCode =:postalCode and t.user =:user")
    List<WeatherRequest> findByPostalCodeOrUser(@Param("postalCode") String postalCode, @Param("user") String user);
	@Query("Select t from WeatherRequest t where t.postalCode =:postalCode")
	List<WeatherRequest> findByPostalCode(@Param("postalCode") String postalCode);
	@Query("Select t from WeatherRequest t where t.user =:user")
	List<WeatherRequest> findByUser(@Param("user") String user);
}
