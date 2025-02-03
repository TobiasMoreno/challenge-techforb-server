package com.techforb.challenge_server.services.impl;

import com.techforb.challenge_server.common.mapper.ModelMapperUtils;
import com.techforb.challenge_server.dtos.sensor.ResponseSensorDTO;
import com.techforb.challenge_server.repositories.SensorRepository;
import com.techforb.challenge_server.services.SensorService;
import com.techforb.challenge_server.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SensorServiceImpl implements SensorService {

	private final SensorRepository sensorRepository;
	private final ModelMapperUtils modelMapperUtils;
	private final UserService userService;

	@Override
	public List<ResponseSensorDTO> findByAvailableFalseAndUser_Email() {
		String userEmail = userService.getCurrentUserEmail();
		return modelMapperUtils.mapAll(sensorRepository.findByIsAvailableFalseAndUser_Email(userEmail), ResponseSensorDTO.class);
	}
}
