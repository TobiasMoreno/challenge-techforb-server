package com.techforb.challenge_server.services;

import com.techforb.challenge_server.dtos.alert.RequestAlertDTO;
import com.techforb.challenge_server.dtos.alert.ResponseAlertDTO;
import com.techforb.challenge_server.entities.ReadingEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AlertService {
	List<ResponseAlertDTO> getAlerts();
	List<ResponseAlertDTO> getAlertsByReadingId(ReadingEntity readingEntity);
	ResponseAlertDTO createAlert(RequestAlertDTO alertDTO, ReadingEntity readingEntity);

	ResponseAlertDTO updateAlert(Long id, RequestAlertDTO alertDTO);
}
