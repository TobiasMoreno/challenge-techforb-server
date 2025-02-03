package com.techforb.challenge_server.services;

import com.techforb.challenge_server.dtos.sensor.ResponseSensorDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SensorService {
	List<ResponseSensorDTO> findByAvailableFalseAndUser_Email();
}
