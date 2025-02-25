package com.example.dao;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.models.Weather;
import org.springframework.data.mongodb.repository.Query;

public interface WeatherRepository extends MongoRepository<Weather, String> {
	
	Optional<Weather> findByCountry(String country);
	//Optional<List<Weather>> findByCountryEqualsIgnoreCase(String country);

}


//logger add krne hai
//custom Controller ADvise annotation use krna hai for exceptional handling
//CRUD perform krna hai