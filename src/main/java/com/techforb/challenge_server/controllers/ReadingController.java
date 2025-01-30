package com.techforb.challenge_server.controllers;

import com.techforb.challenge_server.services.ReadingService;
import com.techforb.challenge_server.dtos.reading.ResponseReadingDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/readings")
@RequiredArgsConstructor
public class ReadingController {

	private final ReadingService readingService;

	@GetMapping("list")
	public ResponseEntity<List<ResponseReadingDTO>> getAllReadings() {
		List<ResponseReadingDTO> readingDTOS = readingService.getReadings();
		return ResponseEntity.ok(readingDTOS);
	}
}
