package com.techforb.challenge_server.controllers;

import com.techforb.challenge_server.dtos.sensor.ResponseSensorDTO;
import com.techforb.challenge_server.services.SensorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
class SensorControllerTest {

	@Autowired
	private SensorController sensorController;

	@MockBean
	private SensorService sensorService;

	private MockMvc mockMvc;
	private ResponseSensorDTO responseSensorDTO;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(sensorController).build();
		responseSensorDTO = new ResponseSensorDTO();
		responseSensorDTO.setAvailable(false);
	}

	@Test
	void getDisabledSensors_ShouldReturnDisabledSensors() throws Exception {
		when(sensorService.findByAvailableFalseAndUser_Email())
				.thenReturn(Collections.singletonList(responseSensorDTO));

		mockMvc.perform(get("/api/sensors/disabled"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].available").value(false));

	}
}