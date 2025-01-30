package com.techforb.challenge_server.dtos.alert;

import com.techforb.challenge_server.dtos.reading.ResponseReadingDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseAlertDTO {
	private Long id;
	private String type;
	private String message;
	private ResponseReadingDTO reading;
}
