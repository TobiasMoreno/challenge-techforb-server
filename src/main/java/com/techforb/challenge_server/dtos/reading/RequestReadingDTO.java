package com.techforb.challenge_server.dtos.reading;

import com.techforb.challenge_server.dtos.alert.RequestAlertDTO;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class RequestReadingDTO {

	private Double readingValue;

	private LocalDateTime timestamp;

	private Long sensorId;

	private List<RequestAlertDTO> alerts;
}
