package com.techforb.challenge_server.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.techforb.challenge_server.dtos.reading.RequestReadingDTO;
import com.techforb.challenge_server.dtos.reading.RequestUpdateReadingDTO;
import com.techforb.challenge_server.dtos.reading.ResponseReadingDTO;
import com.techforb.challenge_server.entities.UserEntity;
import com.techforb.challenge_server.services.ReadingService;
import com.techforb.challenge_server.services.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class ReadingControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private ReadingService readingService;

	@MockBean
	private UserService userService;

	private String jwtToken = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."; // Simulación de un token JWT válido

	private UserEntity testUser;

	@BeforeEach
	void setUp() {
		testUser = new UserEntity();
		testUser.setId(1L);
		testUser.setEmail("test@example.com");

		when(userService.getCurrentUserEmail()).thenReturn(testUser.getEmail());
	}

	@Test
	void getAllReadings_ShouldReturnListOfReadings() throws Exception {
		ResponseReadingDTO responseReadingDTO = new ResponseReadingDTO();
		ResponseReadingDTO sresponseReadingDTO = new ResponseReadingDTO();
		List<ResponseReadingDTO> responseReadingDTOList = new ArrayList<>();
		responseReadingDTOList.add(responseReadingDTO);
		responseReadingDTOList.add(sresponseReadingDTO);
		when(readingService.getReadings(testUser.getEmail())).thenReturn(responseReadingDTOList);

		mockMvc.perform(get("/api/readings/list")
						.header("Authorization", jwtToken))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.*", hasSize(2)))
				.andExpect(jsonPath("$[0]").exists());
	}

	@Test
	void getAllReadings_ShouldReturnEmptyList() throws Exception {
		when(readingService.getReadings(testUser.getEmail())).thenReturn(List.of());

		mockMvc.perform(get("/api/readings/list").header("Authorization", jwtToken))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(0)));
	}

	@Test
	void getAllReadings_ShouldReturnUnauthorized() throws Exception {
		mockMvc.perform(get("/api/readings/list"))
				.andExpect(status().isUnauthorized());
	}


	@Test
	void getReadingById_ShouldReturnReading() throws Exception {
		Long readingId = 1L;
		ResponseReadingDTO responseReadingDTO = new ResponseReadingDTO();

		when(readingService.getReadingById(readingId, "test@example.com")).thenReturn(responseReadingDTO);

		mockMvc.perform(get("/api/readings/get/{id}", readingId)
						.header("Authorization", jwtToken))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").exists());
	}


	@Test
	void createReading_ShouldReturnCreatedReading() throws Exception {
		RequestReadingDTO requestReadingDTO = new RequestReadingDTO();
		ResponseReadingDTO responseReadingDTO = new ResponseReadingDTO();

		when(readingService.createReading(any(RequestReadingDTO.class), eq("test@example.com")))
				.thenReturn(responseReadingDTO);

		mockMvc.perform(post("/api/readings/create")
						.header("Authorization", jwtToken)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(requestReadingDTO)))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$").exists());
	}

	@Test
	void createReading_ShouldReturnBadRequest() throws Exception {
		mockMvc.perform(post("/api/readings/create")
						.header("Authorization", jwtToken)
						.contentType("application/json")
						.content(""))
				.andExpect(status().isBadRequest());
	}


	@Test
	void updateReading_ShouldReturnUpdatedReading() throws Exception {
		Long readingId = 1L;
		RequestUpdateReadingDTO requestUpdateReadingDTO = new RequestUpdateReadingDTO();
		ResponseReadingDTO updatedReadingDTO = new ResponseReadingDTO();

		when(readingService.updateReading(eq(readingId), any(RequestUpdateReadingDTO.class), eq("test@example.com")))
				.thenReturn(updatedReadingDTO);

		mockMvc.perform(put("/api/readings/update/{id}", readingId)
						.header("Authorization", jwtToken)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(requestUpdateReadingDTO)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").exists());
	}

	@Test
	void updateReading_ShouldReturnNotFound() throws Exception {
		when(readingService.updateReading(1L, new RequestUpdateReadingDTO(), testUser.getEmail()))
				.thenThrow(new RuntimeException("Not found"));

		mockMvc.perform(put("/api/readings/update/1")
						.header("Authorization", jwtToken)
						.contentType("application/json")
						.content("{}"))
				.andExpect(status().isNotFound());
	}


	@Test
	void deleteReading_ShouldReturnNoContent() throws Exception {
		Long readingId = 1L;

		doNothing().when(readingService).deleteReading(readingId, "test@example.com");

		mockMvc.perform(delete("/api/readings/{id}", readingId)
						.header("Authorization", jwtToken))
				.andExpect(status().isNoContent());
	}

	@Test
	void deleteReading_ShouldReturnNotFound() throws Exception {
		// Simula que el servicio lanza la excepción
		doThrow(new EntityNotFoundException("No se encontraron lecturas con ese id"))
				.when(readingService).deleteReading(1L, testUser.getEmail());

		mockMvc.perform(delete("/api/readings/1")
						.header("Authorization", jwtToken))
				.andExpect(status().isNotFound())  // Espero 404 pero me devuelve 500
				.andExpect(jsonPath("$.message").value("No se encontraron lecturas con ese id"));  // Verifica el mensaje
	}

}
