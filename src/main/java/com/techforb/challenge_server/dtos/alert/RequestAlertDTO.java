package com.techforb.challenge_server.dtos.alert;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestAlertDTO {
	private String type;
	private String message;
	private Long readingId;
}
