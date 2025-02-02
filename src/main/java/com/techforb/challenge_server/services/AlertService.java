package com.techforb.challenge_server.services;

import com.techforb.challenge_server.dtos.alert.RequestAlertDTO;
import com.techforb.challenge_server.dtos.alert.ResponseAlertDTO;
import com.techforb.challenge_server.entities.ReadingEntity;
import com.techforb.challenge_server.models.AlertType;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AlertService {
	List<ResponseAlertDTO> getAllAlerts();
	ResponseAlertDTO getAlertById(Long alertId);
	List<ResponseAlertDTO> getAlertsByType(AlertType type);
	ResponseAlertDTO createAlert(RequestAlertDTO requestAlertDTO);
	ResponseAlertDTO updateAlertById(Long id, RequestAlertDTO requestAlertDTO);
	void deleteAlertById(Long alertId);
	List<ResponseAlertDTO> getAlertsByReadingId(ReadingEntity readingEntity);
}
