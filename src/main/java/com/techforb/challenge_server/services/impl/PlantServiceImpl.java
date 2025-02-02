package com.techforb.challenge_server.services.impl;

import com.techforb.challenge_server.common.mapper.ModelMapperUtils;
import com.techforb.challenge_server.dtos.plant.RequestPlantDTO;
import com.techforb.challenge_server.dtos.plant.ResponsePlantDTO;
import com.techforb.challenge_server.dtos.user.ResponseUserDTO;
import com.techforb.challenge_server.entities.PlantEntity;
import com.techforb.challenge_server.entities.UserEntity;
import com.techforb.challenge_server.repositories.PlantRepository;
import com.techforb.challenge_server.services.PlantService;
import com.techforb.challenge_server.services.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlantServiceImpl implements PlantService {

	private final PlantRepository plantRepository;
	private final UserService userService;
	private final ModelMapperUtils modelMapperUtils;

	@Override
	public List<ResponsePlantDTO> getAllPlants(String userEmail) {
		return modelMapperUtils.mapAll(
				plantRepository.findAllByOwner_Email(userEmail), ResponsePlantDTO.class);
	}

	@Override
	public ResponsePlantDTO getPlantById(Long id, String userEmail) {
		return plantRepository.findByIdAndOwner_Email(id, userEmail)
				.map(plant -> modelMapperUtils.map(plant, ResponsePlantDTO.class))
				.orElseThrow(() -> new EntityNotFoundException("Planta no encontrada para el usuario: " + userEmail));
	}

	@Override
	public ResponsePlantDTO createPlant(RequestPlantDTO requestPlantDTO, String userEmail) {
		ResponseUserDTO responseUserDTO = userService.getUserByEmail(userEmail);

		if (responseUserDTO == null) {
			throw new EntityNotFoundException("Usuario no encontrado: " + userEmail);
		}

		PlantEntity plantEntity = modelMapperUtils.map(requestPlantDTO, PlantEntity.class);
		plantEntity.setOwner(modelMapperUtils.map(responseUserDTO, UserEntity.class));

		plantRepository.save(plantEntity);

		return modelMapperUtils.map(plantEntity, ResponsePlantDTO.class);
	}

	@Override
	public ResponsePlantDTO updatePlantById(Long id, RequestPlantDTO requestPlantDTO, String userEmail) {
		PlantEntity plantEntity = plantRepository.findByIdAndOwner_Email(id, userEmail)
				.orElseThrow(() -> new EntityNotFoundException("La planta no existe para el usuario: " + userEmail));

		plantEntity.setName(requestPlantDTO.getName());
		plantRepository.save(plantEntity);

		return modelMapperUtils.map(plantEntity, ResponsePlantDTO.class);
	}

	@Override
	public void deletePlantById(Long id, String userEmail) {
		PlantEntity plantEntity = plantRepository.findByIdAndOwner_Email(id, userEmail)
				.orElseThrow(() -> new EntityNotFoundException("La planta no existe para el usuario: " + userEmail));

		plantRepository.delete(plantEntity);
	}
}
