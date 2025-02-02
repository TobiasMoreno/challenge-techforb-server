package com.techforb.challenge_server.dtos.reading;

import com.techforb.challenge_server.dtos.alert.ResponseAlertDTO;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class RequestUpdateReadingDTO {

	private Double readingValue;

	private LocalDateTime timestamp;

	private Long sensorId;

	private List<ResponseAlertDTO> alerts;
}
