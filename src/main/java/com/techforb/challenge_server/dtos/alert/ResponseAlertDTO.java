package com.techforb.challenge_server.dtos.alert;

import com.techforb.challenge_server.dtos.reading.ResponseReadingDTO;
import com.techforb.challenge_server.models.AlertType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseAlertDTO {
	private Long id;
	private AlertType type;
	private String message;
}
