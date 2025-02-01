package com.techforb.challenge_server.controllers;

import com.techforb.challenge_server.dtos.reading.RequestReadingDTO;
import com.techforb.challenge_server.dtos.reading.RequestUpdateReadingDTO;
import com.techforb.challenge_server.services.ReadingService;
import com.techforb.challenge_server.dtos.reading.ResponseReadingDTO;
import com.techforb.challenge_server.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/readings")
@RequiredArgsConstructor
public class ReadingController {

	private final ReadingService readingService;
	private final UserService userService;


	@GetMapping("/list")
	public ResponseEntity<List<ResponseReadingDTO>> getAllReadings() {
		List<ResponseReadingDTO> readingDTOS = readingService.getReadings(userService.getCurrentUserEmail());
		return ResponseEntity.ok(readingDTOS);
	}
	@GetMapping("/get/{id}" )
	public ResponseEntity<ResponseReadingDTO> getReadingById(@PathVariable("id") Long id) {
		ResponseReadingDTO readingDTO = readingService.getReadingById(id,userService.getCurrentUserEmail());
		return ResponseEntity.ok(readingDTO);
	}

	@PostMapping("/create" )
	public ResponseEntity<ResponseReadingDTO> createReading(@RequestBody RequestReadingDTO requestReadingDTO) {
		ResponseReadingDTO readingDTO = readingService.createReading(requestReadingDTO,userService.getCurrentUserEmail());
		return ResponseEntity.status(HttpStatus.CREATED).body(readingDTO);
	}

	@PutMapping("/update/{id}" )
	public ResponseEntity<ResponseReadingDTO> updateReading(@PathVariable("id") Long id, @RequestBody RequestUpdateReadingDTO requestUpdateReadingDTO) {
		ResponseReadingDTO readingDTO = readingService.updateReading(id,requestUpdateReadingDTO,userService.getCurrentUserEmail());
		return ResponseEntity.ok(readingDTO);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteReading(@PathVariable("id") Long id) {
		readingService.deleteReading(id,userService.getCurrentUserEmail());
		return ResponseEntity.noContent().build();
	}
}
