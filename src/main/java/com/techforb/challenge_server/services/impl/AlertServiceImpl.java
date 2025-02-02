package com.techforb.challenge_server.services.impl;

import com.techforb.challenge_server.common.mapper.ModelMapperUtils;
import com.techforb.challenge_server.dtos.alert.RequestAlertDTO;
import com.techforb.challenge_server.dtos.alert.ResponseAlertDTO;
import com.techforb.challenge_server.entities.AlertEntity;
import com.techforb.challenge_server.entities.ReadingEntity;
import com.techforb.challenge_server.entities.UserEntity;
import com.techforb.challenge_server.models.AlertType;
import com.techforb.challenge_server.repositories.AlertRepository;
import com.techforb.challenge_server.repositories.ReadingRepository;
import com.techforb.challenge_server.repositories.UserRepository;
import com.techforb.challenge_server.services.AlertService;
import com.techforb.challenge_server.services.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AlertServiceImpl implements AlertService {

	private final AlertRepository alertRepository;
	private final UserRepository userRepository;
	private final ReadingRepository readingRepository;
	private final ModelMapperUtils modelMapperUtils;
	private final UserService userService;

	@Override
	public List<ResponseAlertDTO> getAllAlerts() {
		String userEmail = userService.getCurrentUserEmail();
		return modelMapperUtils.mapAll(
				alertRepository.findAllByUser_Email(userEmail), ResponseAlertDTO.class);
	}

	@Override
	public ResponseAlertDTO getAlertById(Long id) {
		String userEmail = userService.getCurrentUserEmail();
		return alertRepository.findByIdAndUser_Email(id, userEmail)
				.map(alert -> modelMapperUtils.map(alert, ResponseAlertDTO.class))
				.orElseThrow(() -> new EntityNotFoundException("Alerta no encontrada para el usuario: " + userEmail));
	}

	@Override
	public ResponseAlertDTO createAlert(RequestAlertDTO requestAlertDTO) {
		String userEmail = userService.getCurrentUserEmail();
		ReadingEntity readingEntity = readingRepository.findById(requestAlertDTO.getReadingId())
				.orElseThrow(() -> new EntityNotFoundException("Lectura no encontrada"));

		UserEntity userEntity = userRepository.findByEmail(userEmail)
				.orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));

		AlertEntity alertEntity = modelMapperUtils.map(requestAlertDTO, AlertEntity.class);
		alertEntity.setReading(readingEntity);
		alertEntity.setUser(userEntity);
		alertRepository.save(alertEntity);

		return modelMapperUtils.map(alertEntity, ResponseAlertDTO.class);
	}

	@Override
	public ResponseAlertDTO updateAlertById(Long id, RequestAlertDTO requestAlertDTO) {
		String userEmail = userService.getCurrentUserEmail();
		AlertEntity alertEntity = alertRepository.findByIdAndUser_Email(id, userEmail)
				.orElseThrow(() -> new EntityNotFoundException("La alerta no existe para el usuario: " + userEmail));

		alertEntity.setType(requestAlertDTO.getType());
		alertEntity.setMessage(requestAlertDTO.getMessage());
		alertRepository.save(alertEntity);

		return modelMapperUtils.map(alertEntity, ResponseAlertDTO.class);
	}

	@Override
	public void deleteAlertById(Long id) {
		String userEmail = userService.getCurrentUserEmail();

		AlertEntity alertEntity = alertRepository.findByIdAndUser_Email(id, userEmail)
				.orElseThrow(() -> new EntityNotFoundException("La alerta no existe para el usuario: " + userEmail));

		alertRepository.delete(alertEntity);
	}

	@Override
	public List<ResponseAlertDTO> getAlertsByType(AlertType type) {
		String userEmail = userService.getCurrentUserEmail();
		List<AlertEntity> alertEntities = alertRepository.findAllByTypeAndUser_Email(type, userEmail);
		if (alertEntities.isEmpty()) {
			throw new EntityNotFoundException("No hay alertas de tipo " + type + " para el usuario: " + userEmail);
		}
		return modelMapperUtils.mapAll(alertEntities, ResponseAlertDTO.class);
	}

	@Override
	public List<ResponseAlertDTO> getAlertsByReadingId(ReadingEntity readingEntity) {
		if (readingEntity == null) {
			throw new IllegalArgumentException("El objeto ReadingEntity no puede ser nulo.");
		}

		String userEmail = userService.getCurrentUserEmail();
		if (userEmail == null || userEmail.isBlank()) {
			throw new IllegalStateException("No se pudo obtener el email del usuario autenticado.");
		}

		List<AlertEntity> alertEntities = alertRepository.findAllByReading_IdAndUser_Email(readingEntity.getId(), userEmail);
		if (alertEntities.isEmpty()) {
			throw new EntityNotFoundException("No hay alertas asociadas a la lectura para el usuario: " + userEmail);
		}

		return modelMapperUtils.mapAll(alertEntities, ResponseAlertDTO.class);
	}

}
