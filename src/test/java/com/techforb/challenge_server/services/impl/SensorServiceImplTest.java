package com.techforb.challenge_server.services.impl;

import com.techforb.challenge_server.common.mapper.ModelMapperUtils;
import com.techforb.challenge_server.dtos.sensor.ResponseSensorDTO;
import com.techforb.challenge_server.dtos.sensor.ResponseSensorStatsDTO;
import com.techforb.challenge_server.entities.AlertEntity;
import com.techforb.challenge_server.entities.ReadingEntity;
import com.techforb.challenge_server.entities.SensorEntity;
import com.techforb.challenge_server.entities.UserEntity;
import com.techforb.challenge_server.models.AlertType;
import com.techforb.challenge_server.repositories.SensorRepository;
import com.techforb.challenge_server.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
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

	private SensorEntity sensor1;
	private SensorEntity sensor2;
	private AlertEntity mediumAlert;
	private AlertEntity redAlert;

	@BeforeEach
	void setUp() {
		UserEntity testUser = new UserEntity();
		testUser.setEmail("test@example.com");

		disabledSensor = new SensorEntity();
		disabledSensor.setAvailable(false);
		disabledSensor.setUser(testUser);

		responseSensorDTO = new ResponseSensorDTO();
		responseSensorDTO.setAvailable(false);

		mediumAlert = new AlertEntity();
		mediumAlert.setType(AlertType.MEDIA);

		redAlert = new AlertEntity();
		redAlert.setType(AlertType.ROJA);

		sensor1 = new SensorEntity();
		sensor1.setId(1L);
		sensor1.setType("Temperature");
		sensor1.setUser(testUser);
		sensor1.setAlerts(Arrays.asList(mediumAlert, redAlert));

		sensor2 = new SensorEntity();
		sensor2.setId(2L);
		sensor2.setType("Humidity");
		sensor2.setUser(testUser);
		sensor2.setAlerts(Collections.emptyList());

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

	@Test
	void getSensorStatsByPlant_ShouldReturnStats() {
		when(sensorRepository.findAllByUser_EmailAndPlant_Id("test@example.com", 1L))
				.thenReturn(Arrays.asList(sensor1, sensor2));

		List<ResponseSensorStatsDTO> result = sensorService.getSensorStatsByPlant(1L);

		assertNotNull(result);
		assertEquals(2, result.size());

		ResponseSensorStatsDTO sensorStats1 = result.stream()
				.filter(s -> s.getSensorId() == 1L)
				.findFirst()
				.orElse(null);

		ResponseSensorStatsDTO sensorStats2 = result.stream()
				.filter(s -> s.getSensorId() == 2L)
				.findFirst()
				.orElse(null);

		assertNotNull(sensorStats1);
		assertEquals("Temperature", sensorStats1.getSensorType());
		assertEquals(0, sensorStats1.getReadingsOk());
		assertEquals(1, sensorStats1.getMediumAlerts());
		assertEquals(1, sensorStats1.getRedAlerts());

		assertNotNull(sensorStats2);
		assertEquals("Humidity", sensorStats2.getSensorType());
		assertEquals(0, sensorStats2.getReadingsOk());
		assertEquals(0, sensorStats2.getMediumAlerts());
		assertEquals(0, sensorStats2.getRedAlerts());

		verify(userService, times(1)).getCurrentUserEmail();
		verify(sensorRepository, times(1)).findAllByUser_EmailAndPlant_Id("test@example.com", 1L);
	}

	@Test
	void getSensorStatsByPlant_ShouldReturnEmptyList() {
		when(sensorRepository.findAllByUser_EmailAndPlant_Id("test@example.com", 1L))
				.thenReturn(Collections.emptyList());

		List<ResponseSensorStatsDTO> result = sensorService.getSensorStatsByPlant(1L);

		assertNotNull(result);
		assertTrue(result.isEmpty());

		verify(userService, times(1)).getCurrentUserEmail();
		verify(sensorRepository, times(1)).findAllByUser_EmailAndPlant_Id("test@example.com", 1L);
	}
}