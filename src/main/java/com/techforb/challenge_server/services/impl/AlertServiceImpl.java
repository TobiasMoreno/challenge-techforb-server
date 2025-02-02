package com.techforb.challenge_server.services.impl;

import com.techforb.challenge_server.common.mapper.ModelMapperUtils;
import com.techforb.challenge_server.dtos.alert.RequestAlertDTO;
import com.techforb.challenge_server.dtos.alert.ResponseAlertDTO;
import com.techforb.challenge_server.entities.AlertEntity;
import com.techforb.challenge_server.entities.ReadingEntity;
import com.techforb.challenge_server.repositories.AlertRepository;
import com.techforb.challenge_server.services.AlertService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AlertServiceImpl implements AlertService {

	private final AlertRepository alertRepository;
	private final ModelMapperUtils modelMapperUtils;

	@Override
	public List<ResponseAlertDTO> getAlerts() {
		List<AlertEntity> alertsEntities = alertRepository.findAll();

		List<ResponseAlertDTO> responseAlertDTOS = new ArrayList<>();

		for (AlertEntity alertEntity : alertsEntities) {
			ResponseAlertDTO responseAlertDTO = modelMapperUtils.map(alertEntity, ResponseAlertDTO.class);
			responseAlertDTOS.add(responseAlertDTO);
		}
		return responseAlertDTOS;
	}

	@Override
	public List<ResponseAlertDTO> getAlertsByReadingId(ReadingEntity readingEntity) {
		List<AlertEntity> alertsEntities = alertRepository.findAllByReading(readingEntity);
		List<ResponseAlertDTO> responseAlertDTOS = new ArrayList<>();

		for (AlertEntity alertEntity : alertsEntities) {
			ResponseAlertDTO responseAlertDTO = modelMapperUtils.map(alertEntity, ResponseAlertDTO.class);
			responseAlertDTOS.add(responseAlertDTO);
		}
		return responseAlertDTOS;
	}

	@Override
	public ResponseAlertDTO createAlert(RequestAlertDTO alertDTO, ReadingEntity readingEntity) {
		AlertEntity alertEntity = modelMapperUtils.map(alertDTO, AlertEntity.class);
		alertEntity.setReading(readingEntity);
		alertEntity = alertRepository.save(alertEntity);

		return modelMapperUtils.map(alertEntity, ResponseAlertDTO.class);
	}

	@Override
	public ResponseAlertDTO updateAlert(Long id, RequestAlertDTO alertDTO) {
		AlertEntity alertEntity = alertRepository.findById(id).orElse(null);

		if (alertEntity == null) {
			throw new IllegalArgumentException("No se encontraron alertas con ese id");
		}

		alertEntity = modelMapperUtils.map(alertDTO, AlertEntity.class);
		alertEntity = alertRepository.save(alertEntity);

		return modelMapperUtils.map(alertEntity, ResponseAlertDTO.class);
	}
}
