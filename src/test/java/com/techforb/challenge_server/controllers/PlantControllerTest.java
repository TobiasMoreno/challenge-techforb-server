package com.techforb.challenge_server.controllers;

import com.techforb.challenge_server.dtos.plant.RequestPlantDTO;
import com.techforb.challenge_server.dtos.plant.ResponsePlantDTO;
import com.techforb.challenge_server.services.PlantService;
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
class PlantControllerTest {

	@MockBean
	private PlantService plantService;

	@Autowired
	private PlantController plantController;

	private MockMvc mockMvc;
	private RequestPlantDTO requestPlantDTO;
	private ResponsePlantDTO responsePlantDTO;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(plantController).build();

		requestPlantDTO = new RequestPlantDTO();
		requestPlantDTO.setName("Plant1");
		requestPlantDTO.setCountry("Country1");

		responsePlantDTO = new ResponsePlantDTO();
		responsePlantDTO.setId(1L);
		responsePlantDTO.setName("Plant1");
		responsePlantDTO.setCountry("Country1");
		responsePlantDTO.setOwnerEmail("user@example.com");
	}

	@Test
	void testGetAllPlants() throws Exception {
		// Setup
		when(plantService.getAllPlants()).thenReturn(List.of(responsePlantDTO));

		mockMvc.perform(get("/api/plants/list"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].name").value("Plant1"));

		verify(plantService, times(1)).getAllPlants();
	}

	@Test
	void testGetPlantById() throws Exception {
		// Setup
		when(plantService.getPlantById(1L)).thenReturn(responsePlantDTO);

		mockMvc.perform(get("/api/plants/{id}", 1L))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.name").value("Plant1"));

		verify(plantService, times(1)).getPlantById(1L);
	}

	@Test
	void testCreatePlant() throws Exception {
		// Setup
		when(plantService.createPlant(requestPlantDTO)).thenReturn(responsePlantDTO);

		mockMvc.perform(post("/api/plants")
						.contentType("application/json")
						.content("{\"name\": \"Plant1\", \"country\": \"Country1\"}"))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.name").value("Plant1"))
				.andExpect(jsonPath("$.country").value("Country1"));

		verify(plantService, times(1)).createPlant(requestPlantDTO);
	}

	@Test
	void testCreatePlant_CountryEmpty_ShouldReturnBadRequest() throws Exception {
		mockMvc.perform(post("/api/plants")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"name\": \"New Plant\", \"country\": \"\"}"))
				.andExpect(status().isBadRequest());
	}

	@Test
	void testCreatePlant_NamePlantEmpty_ShouldReturnBadRequest() throws Exception {
		mockMvc.perform(post("/api/plants")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"name\": \"\", \"country\": \"Argentina\"}"))
				.andExpect(status().isBadRequest());
	}

	@Test
	void testUpdatePlantById() throws Exception {
		requestPlantDTO.setName("Updated Plant");
		requestPlantDTO.setCountry("Updated Country");

		responsePlantDTO.setName("Updated Plant");
		responsePlantDTO.setCountry("Updated Country");

		when(plantService.updatePlantById(1L, requestPlantDTO)).thenReturn(responsePlantDTO);

		mockMvc.perform(put("/api/plants/{id}", 1L)
						.contentType("application/json")
						.content("{\"name\": \"Updated Plant\", \"country\": \"Updated Country\"}"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.name").value("Updated Plant"))
				.andExpect(jsonPath("$.country").value("Updated Country"));
	}


	@Test
	void testDeletePlantById() throws Exception {
		// Setup
		doNothing().when(plantService).deletePlantById(1L);

		mockMvc.perform(delete("/api/plants/{id}", 1L))
				.andExpect(status().isNoContent());

		verify(plantService, times(1)).deletePlantById(1L);
	}
}
