package com.techforb.challenge_server.services.impl;

import com.techforb.challenge_server.common.mapper.ModelMapperUtils;
import com.techforb.challenge_server.dtos.alert.RequestAlertDTO;
import com.techforb.challenge_server.dtos.alert.ResponseAlertDTO;
import com.techforb.challenge_server.dtos.reading.RequestReadingDTO;
import com.techforb.challenge_server.dtos.reading.RequestUpdateReadingDTO;
import com.techforb.challenge_server.dtos.sensor.ResponseSensorDTO;
import com.techforb.challenge_server.dtos.user.ResponseUserDTO;
import com.techforb.challenge_server.entities.AlertEntity;
import com.techforb.challenge_server.entities.ReadingEntity;
import com.techforb.challenge_server.dtos.reading.ResponseReadingDTO;
import com.techforb.challenge_server.entities.UserEntity;
import com.techforb.challenge_server.models.User;
import com.techforb.challenge_server.repositories.ReadingRepository;
import com.techforb.challenge_server.services.AlertService;
import com.techforb.challenge_server.services.ReadingService;
import com.techforb.challenge_server.services.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReadingServiceImpl implements ReadingService {

	private final ReadingRepository readingRepository;
	private final ModelMapperUtils modelMapperUtils;
	private final AlertService alertService;
	private final UserService userService;

	@Override
	public List<ResponseReadingDTO> getReadings(String currentUserEmail) {

		List<ResponseReadingDTO> readings = new ArrayList<>();
		List<ReadingEntity> readingEntities = readingRepository.findAllByUser_Email(currentUserEmail);

		if (readingEntities.isEmpty()) {
			throw new IllegalArgumentException("No se encontraron lecturas");
		}

		for (ReadingEntity readingEntity : readingEntities) {
			ResponseReadingDTO responseReadingDTO = modelMapperUtils.map(readingEntity, ResponseReadingDTO.class);
			responseReadingDTO.setSensor(modelMapperUtils.map(readingEntity.getSensor(),ResponseSensorDTO.class));
			responseReadingDTO.setAlerts(alertService.getAlertsByReadingId(readingEntity));

			readings.add(responseReadingDTO);
		}
		return readings;
	}

	@Override
	public ResponseReadingDTO getReadingById(Long id, String currentUserEmail) {
		ReadingEntity readingEntity = readingRepository.findById(id).orElse(null);
		if (readingEntity == null) {
			throw new EntityNotFoundException("No se encontraron lecturas con ese id");
		}
		if(validateEmail(readingEntity,currentUserEmail)) {
			throw new IllegalArgumentException("Acceso no Autorizado");
		}

		ResponseReadingDTO responseReadingDTO = modelMapperUtils.map(readingEntity, ResponseReadingDTO.class);
		responseReadingDTO.setSensor(modelMapperUtils.map(readingEntity.getSensor(),ResponseSensorDTO.class));
		responseReadingDTO.setAlerts(alertService.getAlertsByReadingId(readingEntity));
		return responseReadingDTO;
	}

	@Override
	public ResponseReadingDTO createReading(RequestReadingDTO readingDTO, String userEmail) {
		UserEntity userEntity = userService.getCurrentUserEntity();

		ReadingEntity readingEntity = modelMapperUtils.map(readingDTO, ReadingEntity.class);
		readingEntity.setUser(userEntity);

		List<AlertEntity> alertEntities = modelMapperUtils.mapAll(readingDTO.getAlerts(), AlertEntity.class);
		readingEntity.setAlerts(alertEntities);

		readingRepository.save(readingEntity);

		return modelMapperUtils.map(readingEntity, ResponseReadingDTO.class);
	}

	@Override
	public ResponseReadingDTO updateReading(Long id, RequestUpdateReadingDTO readingDTO,String currentUserEmail) {
		ReadingEntity readingEntity = readingRepository.findById(id).orElse(null);

		if (readingEntity == null) {
			throw new EntityNotFoundException("No se encontraron lecturas con ese id");
		}

		if(validateEmail(readingEntity,currentUserEmail)) {
			throw new IllegalArgumentException("Acceso no Autorizado");
		}

		readingEntity = modelMapperUtils.map(readingDTO, ReadingEntity.class);
		if(readingDTO.getAlerts() != null) {
			List<AlertEntity> alertEntities = modelMapperUtils.mapAll(readingDTO.getAlerts(), AlertEntity.class);
			readingEntity.setAlerts(alertEntities);
		}

		return modelMapperUtils.map(readingEntity, ResponseReadingDTO.class);
	}

	@Override
	public void deleteReading(Long id,String currentUserEmail) {
		ReadingEntity readingEntity = readingRepository.findById(id).orElse(null);
		if (readingEntity == null) {
			throw new EntityNotFoundException("No se encontraron lecturas con ese id");
		}

		if(validateEmail(readingEntity,currentUserEmail)) {
			throw new IllegalArgumentException("Acceso no Autorizado");
		}

		readingRepository.delete(readingEntity);
	}

	private boolean validateEmail(ReadingEntity readingEntity,String currentUserEmail) {
		return !readingEntity.getUser().getEmail().equals(currentUserEmail);
	}
}
