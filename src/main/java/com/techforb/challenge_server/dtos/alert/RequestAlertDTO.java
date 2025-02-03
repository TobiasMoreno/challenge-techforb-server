package com.techforb.challenge_server.dtos.alert;

import com.techforb.challenge_server.models.AlertType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestAlertDTO {
	private AlertType type;
	private String message;
	private Long readingId;
}
