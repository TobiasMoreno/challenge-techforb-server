package com.techforb.challenge_server.dtos.reading;

import com.techforb.challenge_server.dtos.alert.ResponseAlertDTO;
import com.techforb.challenge_server.dtos.sensor.ResponseSensorDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseReadingDTO {
	private Long id;
	private double readingValue;
	private LocalDateTime timestamp;
	private ResponseSensorDTO sensor;
	private List<ResponseAlertDTO> alerts;
}
