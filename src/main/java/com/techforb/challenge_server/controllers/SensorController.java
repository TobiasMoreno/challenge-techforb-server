package com.techforb.challenge_server.controllers;

import com.techforb.challenge_server.common.dto.ApiError;
import com.techforb.challenge_server.dtos.sensor.ResponseSensorDTO;
import com.techforb.challenge_server.services.SensorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/sensors")
@RequiredArgsConstructor
public class SensorController {

	private final SensorService sensorService;

	@Operation(summary = "Obtener sensores deshabilitados", description = "Obtiene sensores deshabilitados")
	@ApiResponse(responseCode = "200", description = "Lista de sensores deshabilitados obtenidos correctamente",
			content = @Content(schema = @Schema(implementation = ResponseSensorDTO.class)))
	@ApiResponse(responseCode = "404", description = "No hay sensores deshabilitados encontradas",
			content = @Content(schema = @Schema(implementation = ApiError.class)))
	@GetMapping("/disabled")
	public ResponseEntity<List<ResponseSensorDTO>> getDisabledSensors() {
		return ResponseEntity.ok(sensorService.findByAvailableFalseAndUser_Email());
	}
}
