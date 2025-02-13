package com.techforb.challenge_server.services.impl;

import com.techforb.challenge_server.common.mapper.ModelMapperUtils;
import com.techforb.challenge_server.dtos.alert.ResponseAlertCount;
import com.techforb.challenge_server.dtos.plant.RequestPlantDTO;
import com.techforb.challenge_server.dtos.plant.ResponseCountPlantDTO;
import com.techforb.challenge_server.dtos.plant.ResponsePlantDTO;
import com.techforb.challenge_server.dtos.user.ResponseUserDTO;
import com.techforb.challenge_server.entities.*;
import com.techforb.challenge_server.models.AlertType;
import com.techforb.challenge_server.repositories.PlantRepository;
import com.techforb.challenge_server.services.PlantService;
import com.techforb.challenge_server.services.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PlantServiceImpl implements PlantService {

	private final PlantRepository plantRepository;
	private final UserService userService;
	private final ModelMapperUtils modelMapperUtils;

	@Override
	public List<ResponsePlantDTO> getAllPlants() {
		String userEmail = userService.getCurrentUserEmail();
		return modelMapperUtils.mapAll(
				plantRepository.findAllByOwner_Email(userEmail), ResponsePlantDTO.class);
	}

	@Override
	public ResponsePlantDTO getPlantById(Long id) {
		String userEmail = userService.getCurrentUserEmail();
		return plantRepository.findByIdAndOwner_Email(id, userEmail)
				.map(plant -> modelMapperUtils.map(plant, ResponsePlantDTO.class))
				.orElseThrow(() -> new EntityNotFoundException("Planta no encontrada para el usuario: " + userEmail));
	}

	@Override
	public ResponsePlantDTO createPlant(RequestPlantDTO requestPlantDTO) {
		String userEmail = userService.getCurrentUserEmail();
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
	public ResponsePlantDTO updatePlantById(Long id, RequestPlantDTO requestPlantDTO) {
		String userEmail = userService.getCurrentUserEmail();
		PlantEntity plantEntity = plantRepository.findByIdAndOwner_Email(id, userEmail)
				.orElseThrow(() -> new EntityNotFoundException("La planta no existe para el usuario: " + userEmail));

		if(requestPlantDTO.getCountry() == null || requestPlantDTO.getCountry().isEmpty()) {
			throw new IllegalArgumentException("El país no puede estar vacío");
		}

		plantEntity.setName(requestPlantDTO.getName());
		plantEntity.setCountry(requestPlantDTO.getCountry());
		plantRepository.save(plantEntity);

		return modelMapperUtils.map(plantEntity, ResponsePlantDTO.class);
	}

	@Override
	public void deletePlantById(Long id) {
		String userEmail = userService.getCurrentUserEmail();
		PlantEntity plantEntity = plantRepository.findByIdAndOwner_Email(id, userEmail)
				.orElseThrow(() -> new EntityNotFoundException("La planta no existe para el usuario: " + userEmail));

		plantRepository.delete(plantEntity);
	}

	@Override
	public List<ResponseCountPlantDTO> getCountPlants() {
		String userEmail = userService.getCurrentUserEmail();

		List<PlantEntity> plantEntities = plantRepository.findAllByOwner_Email(userEmail);
		List<ResponseCountPlantDTO> responseCountPlantDTOS = new ArrayList<>();

		for (PlantEntity plantEntity : plantEntities) {
			ResponseCountPlantDTO responseCountPlantDTO = new ResponseCountPlantDTO();
			responseCountPlantDTO.setId(plantEntity.getId());
			responseCountPlantDTO.setName(plantEntity.getName());
			responseCountPlantDTO.setCountry(plantEntity.getCountry());

			ResponseAlertCount responseAlertCount = countAlertsForPlant(plantEntity);

			responseCountPlantDTO.setMediumAlerts(responseAlertCount.getMediumAlerts());
			responseCountPlantDTO.setRedAlerts(responseAlertCount.getRedAlerts());
			responseCountPlantDTO.setReadingsOk(responseAlertCount.getReadingsOk());
			responseCountPlantDTOS.add(responseCountPlantDTO);
		}
		return responseCountPlantDTOS;
	}

	private ResponseAlertCount countAlertsForPlant(PlantEntity plantEntity) {
		int readingsOk = 0;
		int mediumAlerts = 0;
		int redAlerts = 0;

		for (SensorEntity sensorEntity : plantEntity.getSensors()) {
			for (ReadingEntity readingEntity : sensorEntity.getReadings()) {
				if (readingEntity.getAlerts().isEmpty()) {
					readingsOk++;
				} else {
					for (AlertEntity alertEntity : readingEntity.getAlerts()) {
						if (alertEntity.getType().equals(AlertType.MEDIA)) {
							mediumAlerts++;
						} else if (alertEntity.getType().equals(AlertType.ROJA)) {
							redAlerts++;
						}
					}
				}
			}
		}

		return new ResponseAlertCount(readingsOk, mediumAlerts, redAlerts);
	}
}
