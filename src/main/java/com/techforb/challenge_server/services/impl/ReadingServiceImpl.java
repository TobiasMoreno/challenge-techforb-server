package com.techforb.challenge_server.services.impl;

import com.techforb.challenge_server.entities.ReadingEntity;
import com.techforb.challenge_server.dtos.reading.ResponseReadingDTO;
import com.techforb.challenge_server.repositories.ReadingRepository;
import com.techforb.challenge_server.services.ReadingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReadingServiceImpl implements ReadingService {

	private final ReadingRepository readingRepository;

	@Override
	public List<ResponseReadingDTO> getReadings() {
		List<ResponseReadingDTO> readings = new ArrayList<>();
		List<ReadingEntity> readingEntities = readingRepository.findAll();

		if (readingEntities.isEmpty()) {
			throw new IllegalArgumentException("No se encontraron lecturas");
		}

		for (ReadingEntity readingEntity : readingEntities) {
			ResponseReadingDTO responseReadingDTO = new ResponseReadingDTO();
			responseReadingDTO.setId(readingEntity.getId());
			responseReadingDTO.setReadingValue(readingEntity.getReadingValue());
			responseReadingDTO.setTimestamp(readingEntity.getTimestamp());
			responseReadingDTO.setSensor(readingEntity.getSensor().getType());

			List<String> alerts = new ArrayList<>();
			readingEntity.getAlerts().forEach(alert -> alerts.add(alert.getType()));
			responseReadingDTO.setAlerts(alerts);

			readings.add(responseReadingDTO);
		}
		return readings;
	}
}
