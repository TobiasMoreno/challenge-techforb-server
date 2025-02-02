package com.techforb.challenge_server.services.impl;

import com.techforb.challenge_server.common.mapper.ModelMapperUtils;
import com.techforb.challenge_server.dtos.plant.RequestPlantDTO;
import com.techforb.challenge_server.dtos.plant.ResponsePlantDTO;
import com.techforb.challenge_server.dtos.user.ResponseUserDTO;
import com.techforb.challenge_server.entities.PlantEntity;
import com.techforb.challenge_server.entities.UserEntity;
import com.techforb.challenge_server.repositories.PlantRepository;
import com.techforb.challenge_server.services.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class PlantServiceImplTest {

	@MockBean
	private PlantRepository plantRepository;

	@MockBean
	private UserService userService;

	@MockBean
	private ModelMapperUtils modelMapperUtils;

	@Autowired
	private PlantServiceImpl plantService;

	private PlantEntity plantEntity;
	private ResponseUserDTO responseUserDTO;
	private RequestPlantDTO requestPlantDTO;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);

		plantEntity = new PlantEntity(1L, "Plant1", new UserEntity(), null);

		responseUserDTO = new ResponseUserDTO();
		responseUserDTO.setId(1L);
		responseUserDTO.setPassword("password");
		responseUserDTO.setEmail("user@example.com");

		requestPlantDTO = new RequestPlantDTO();
		requestPlantDTO.setName("Plant1");
	}

	@Test
	void testGetAllPlants() {
		String userEmail = "user@example.com";
		when(plantRepository.findAllByOwner_Email(userEmail)).thenReturn(List.of(plantEntity));
		when(modelMapperUtils.mapAll(List.of(plantEntity), ResponsePlantDTO.class)).thenReturn(List.of(new ResponsePlantDTO()));

		List<ResponsePlantDTO> result = plantService.getAllPlants(userEmail);

		assertNotNull(result);
		assertEquals(1, result.size());
		verify(plantRepository, times(1)).findAllByOwner_Email(userEmail);
	}

	@Test
	void testGetPlantById_Success() {
		String userEmail = "user@example.com";
		when(plantRepository.findByIdAndOwner_Email(1L, userEmail)).thenReturn(Optional.of(plantEntity));
		when(modelMapperUtils.map(plantEntity, ResponsePlantDTO.class)).thenReturn(new ResponsePlantDTO());

		ResponsePlantDTO result = plantService.getPlantById(1L, userEmail);

		assertNotNull(result);
		verify(plantRepository, times(1)).findByIdAndOwner_Email(1L, userEmail);
	}

	@Test
	void testGetPlantById_NotFound() {
		String userEmail = "user@example.com";
		when(plantRepository.findByIdAndOwner_Email(1L, userEmail)).thenReturn(Optional.empty());

		EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
			plantService.getPlantById(1L, userEmail);
		});

		assertEquals("Planta no encontrada para el usuario: user@example.com", exception.getMessage());
	}

	@Test
	void testCreatePlant_Success() {
		String userEmail = "user@example.com";
		when(userService.getUserByEmail(userEmail)).thenReturn(responseUserDTO);
		when(modelMapperUtils.map(requestPlantDTO, PlantEntity.class)).thenReturn(plantEntity);
		when(modelMapperUtils.map(responseUserDTO, UserEntity.class)).thenReturn(new UserEntity());
		when(plantRepository.save(plantEntity)).thenReturn(plantEntity);
		when(modelMapperUtils.map(plantEntity, ResponsePlantDTO.class)).thenReturn(new ResponsePlantDTO());

		ResponsePlantDTO result = plantService.createPlant(requestPlantDTO, userEmail);

		assertNotNull(result);
		verify(plantRepository, times(1)).save(plantEntity);
	}

	@Test
	void testCreatePlant_UserNotFound() {
		String userEmail = "user@example.com";
		when(userService.getUserByEmail(userEmail)).thenReturn(null);

		EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
			plantService.createPlant(requestPlantDTO, userEmail);
		});

		assertEquals("Usuario no encontrado: user@example.com", exception.getMessage());
	}

	@Test
	void testUpdatePlantById_Success() {
		String userEmail = "user@example.com";
		when(plantRepository.findByIdAndOwner_Email(1L, userEmail)).thenReturn(Optional.of(plantEntity));
		when(modelMapperUtils.map(requestPlantDTO, PlantEntity.class)).thenReturn(plantEntity);
		when(plantRepository.save(plantEntity)).thenReturn(plantEntity);
		when(modelMapperUtils.map(plantEntity, ResponsePlantDTO.class)).thenReturn(new ResponsePlantDTO());

		ResponsePlantDTO result = plantService.updatePlantById(1L, requestPlantDTO, userEmail);

		assertNotNull(result);
		verify(plantRepository, times(1)).save(plantEntity);
	}

	@Test
	void testUpdatePlantById_NotFound() {
		String userEmail = "user@example.com";
		when(plantRepository.findByIdAndOwner_Email(1L, userEmail)).thenReturn(Optional.empty());

		EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
			plantService.updatePlantById(1L, requestPlantDTO, userEmail);
		});

		assertEquals("La planta no existe para el usuario: user@example.com", exception.getMessage());
	}

	@Test
	void testDeletePlantById_Success() {
		String userEmail = "user@example.com";
		when(plantRepository.findByIdAndOwner_Email(1L, userEmail)).thenReturn(Optional.of(plantEntity));

		plantService.deletePlantById(1L, userEmail);

		verify(plantRepository, times(1)).delete(plantEntity);
	}

	@Test
	void testDeletePlantById_NotFound() {
		String userEmail = "user@example.com";
		when(plantRepository.findByIdAndOwner_Email(1L, userEmail)).thenReturn(Optional.empty());

		EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
			plantService.deletePlantById(1L, userEmail);
		});

		assertEquals("La planta no existe para el usuario: user@example.com", exception.getMessage());
	}
}