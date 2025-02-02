package com.techforb.challenge_server.controllers;

import com.techforb.challenge_server.dtos.reading.RequestReadingDTO;
import com.techforb.challenge_server.dtos.reading.RequestUpdateReadingDTO;
import com.techforb.challenge_server.services.ReadingService;
import com.techforb.challenge_server.dtos.reading.ResponseReadingDTO;
import com.techforb.challenge_server.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

	@Operation(summary = "Obtener todas las lecturas del usuario actual")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Lista de lecturas obtenida con éxito"),
			@ApiResponse(responseCode = "401", description = "No autorizado")
	})
	@GetMapping("/list")
	public ResponseEntity<List<ResponseReadingDTO>> getAllReadings() {
		List<ResponseReadingDTO> readingDTOS = readingService.getReadings(userService.getCurrentUserEmail());
		return ResponseEntity.ok(readingDTOS);
	}

	@Operation(summary = "Obtener una lectura por ID")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Lectura encontrada"),
			@ApiResponse(responseCode = "404", description = "Lectura no encontrada"),
			@ApiResponse(responseCode = "401", description = "No autorizado")
	})
	@GetMapping("/get/{id}" )
	public ResponseEntity<ResponseReadingDTO> getReadingById(@PathVariable("id") Long id) {
		ResponseReadingDTO readingDTO = readingService.getReadingById(id,userService.getCurrentUserEmail());
		return ResponseEntity.ok(readingDTO);
	}

	@Operation(summary = "Crear una nueva lectura")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Lectura creada con éxito"),
			@ApiResponse(responseCode = "400", description = "Solicitud inválida"),
			@ApiResponse(responseCode = "401", description = "No autorizado")
	})
	@PostMapping("/create")
	public ResponseEntity<ResponseReadingDTO> createReading(@RequestBody RequestReadingDTO requestReadingDTO) {
		ResponseReadingDTO readingDTO = readingService.createReading(requestReadingDTO,userService.getCurrentUserEmail());
		return ResponseEntity.status(HttpStatus.CREATED).body(readingDTO);
	}

	@Operation(summary = "Actualizar una lectura existente")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Lectura actualizada con éxito"),
			@ApiResponse(responseCode = "400", description = "Solicitud inválida"),
			@ApiResponse(responseCode = "404", description = "Lectura no encontrada"),
			@ApiResponse(responseCode = "401", description = "No autorizado")
	})
	@PutMapping("/update/{id}" )
	public ResponseEntity<ResponseReadingDTO> updateReading(@PathVariable("id") Long id, @RequestBody RequestUpdateReadingDTO requestUpdateReadingDTO) {
		ResponseReadingDTO readingDTO = readingService.updateReading(id,requestUpdateReadingDTO,userService.getCurrentUserEmail());
		return ResponseEntity.ok(readingDTO);
	}

	@Operation(summary = "Eliminar una lectura por ID")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "204", description = "Lectura eliminada con éxito"),
			@ApiResponse(responseCode = "404", description = "Lectura no encontrada"),
			@ApiResponse(responseCode = "401", description = "No autorizado")
	})
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteReading(@PathVariable("id") Long id) {
		readingService.deleteReading(id,userService.getCurrentUserEmail());
		return ResponseEntity.noContent().build();
	}
}
