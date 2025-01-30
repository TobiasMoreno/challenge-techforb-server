package com.techforb.challenge_server.dtos.reading;

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
	private String sensor;
	private List<String> alerts;
}
