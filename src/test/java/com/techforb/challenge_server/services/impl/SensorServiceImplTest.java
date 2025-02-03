package com.techforb.challenge_server.services.impl;

import com.techforb.challenge_server.common.mapper.ModelMapperUtils;
import com.techforb.challenge_server.dtos.sensor.ResponseSensorDTO;
import com.techforb.challenge_server.entities.SensorEntity;
import com.techforb.challenge_server.entities.UserEntity;
import com.techforb.challenge_server.repositories.SensorRepository;
import com.techforb.challenge_server.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class SensorServiceImplTest {

	@MockBean
	private SensorRepository sensorRepository;

	@MockBean
	private ModelMapperUtils modelMapperUtils;

	@MockBean
	private UserService userService;

	@Autowired
	private SensorServiceImpl sensorService;

	private SensorEntity disabledSensor;
	private ResponseSensorDTO responseSensorDTO;

	@BeforeEach
	void setUp() {
		UserEntity testUser = new UserEntity();
		testUser.setEmail("test@example.com");

		disabledSensor = new SensorEntity();
		disabledSensor.setAvailable(false);
		disabledSensor.setUser(testUser);

		responseSensorDTO = new ResponseSensorDTO();
		responseSensorDTO.setAvailable(false);
		when(userService.getCurrentUserEmail()).thenReturn("test@example.com");

	}

	@Test
	void findByAvailableFalseAndUser_Email_ShouldReturnDisabledSensors() {
		when(sensorRepository.findByIsAvailableFalseAndUser_Email("test@example.com"))
				.thenReturn(Collections.singletonList(disabledSensor));
		when(modelMapperUtils.mapAll(anyList(), eq(ResponseSensorDTO.class)))
				.thenReturn(Collections.singletonList(responseSensorDTO));

		List<ResponseSensorDTO> result = sensorService.findByAvailableFalseAndUser_Email();

		assertNotNull(result);
		assertEquals(1, result.size());
		assertFalse(result.get(0).isAvailable());

		verify(userService, times(1)).getCurrentUserEmail();
		verify(sensorRepository, times(1)).findByIsAvailableFalseAndUser_Email("test@example.com");
		verify(modelMapperUtils, times(1)).mapAll(anyList(), eq(ResponseSensorDTO.class));
	}
}