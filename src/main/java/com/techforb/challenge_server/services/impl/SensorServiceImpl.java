package com.techforb.challenge_server.services.impl;

import com.techforb.challenge_server.common.mapper.ModelMapperUtils;
import com.techforb.challenge_server.dtos.sensor.ResponseSensorDTO;
import com.techforb.challenge_server.dtos.sensor.ResponseSensorStatsDTO;
import com.techforb.challenge_server.entities.SensorEntity;
import com.techforb.challenge_server.models.AlertType;
import com.techforb.challenge_server.repositories.SensorRepository;
import com.techforb.challenge_server.services.SensorService;
import com.techforb.challenge_server.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SensorServiceImpl implements SensorService {

	private final SensorRepository sensorRepository;
	private final ModelMapperUtils modelMapperUtils;
	private final UserService userService;

	@Override
	public List<ResponseSensorDTO> findByAvailableFalseAndUser_Email() {
		String userEmail = userService.getCurrentUserEmail();
		return modelMapperUtils.mapAll(sensorRepository.findByIsAvailableFalseAndUser_Email(userEmail), ResponseSensorDTO.class);
	}

	@Override
	public List<ResponseSensorStatsDTO> getSensorStatsByPlant(Long plantId) {
		String userEmail = userService.getCurrentUserEmail();
		List<SensorEntity> sensors = sensorRepository.findAllByUser_EmailAndPlant_Id(userEmail, plantId);

		return sensors.stream().map(sensor -> {
			int readingsOk = (int) sensor.getReadings().stream()
					.filter(reading -> reading.getAlerts().isEmpty())
					.count();

			int mediumAlerts = (int) sensor.getAlerts().stream()
					.filter(alert -> alert.getType() == AlertType.MEDIA)
					.count();

			int redAlerts = (int) sensor.getAlerts().stream()
					.filter(alert -> alert.getType() == AlertType.ROJA)
					.count();

			return new ResponseSensorStatsDTO(sensor.getId(), sensor.getType(), readingsOk, mediumAlerts, redAlerts);
		}).collect(Collectors.toList());
	}
}
