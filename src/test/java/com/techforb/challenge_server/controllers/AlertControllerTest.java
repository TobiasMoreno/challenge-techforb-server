package com.techforb.challenge_server.controllers;

import com.techforb.challenge_server.dtos.alert.RequestAlertDTO;
import com.techforb.challenge_server.dtos.alert.ResponseAlertDTO;
import com.techforb.challenge_server.models.AlertType;
import com.techforb.challenge_server.services.AlertService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
class AlertControllerTest {

	@Autowired
	private AlertController alertController;

	@MockBean
	private AlertService alertService;

	private MockMvc mockMvc;
	private RequestAlertDTO requestAlertDTO;
	private ResponseAlertDTO responseAlertDTO;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(alertController).build();

		requestAlertDTO = new RequestAlertDTO();
		requestAlertDTO.setMessage("Test Alert");

		responseAlertDTO = new ResponseAlertDTO();
		responseAlertDTO.setId(1L);
		responseAlertDTO.setMessage("Test Alert");
		responseAlertDTO.setType(AlertType.MEDIA);
	}


	@Test
	void testGetAllAlerts() throws Exception {
		when(alertService.getAllAlerts()).thenReturn(List.of(responseAlertDTO));

		mockMvc.perform(get("/api/alerts/list"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].message").value("Test Alert"));

	}

	@Test
	void testGetAlertById() throws Exception {
		when(alertService.getAlertById(1L)).thenReturn(responseAlertDTO);

		mockMvc.perform(get("/api/alerts/{id}", 1L))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.message").value("Test Alert"));

	}


	@Test
	void testGetAlertsByType() throws Exception {
		when(alertService.getAlertsByType(AlertType.MEDIA)).thenReturn(List.of(responseAlertDTO));

		mockMvc.perform(get("/api/alerts/type")
						.param("type", "ALERTA_MEDIA")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].message").value("Test Alert"))
				.andExpect(jsonPath("$[0].type").value("ALERTA_MEDIA"));

	}

	@Test
	void testGetAlertsByType_NotFound() throws Exception {
		when(alertService.getAlertsByType(AlertType.MEDIA)).thenReturn(List.of());

		mockMvc.perform(get("/api/alerts/type")
						.param("type", "ALERTA_MEDIA")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());

	}
}
