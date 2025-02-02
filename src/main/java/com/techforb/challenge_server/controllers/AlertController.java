package com.techforb.challenge_server.controllers;

import com.techforb.challenge_server.common.dto.ApiError;
import com.techforb.challenge_server.dtos.alert.RequestAlertDTO;
import com.techforb.challenge_server.dtos.alert.ResponseAlertDTO;
import com.techforb.challenge_server.models.AlertType;
import com.techforb.challenge_server.services.AlertService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/alerts")
@RequiredArgsConstructor
public class AlertController {

	private final AlertService alertService;

	@Operation(summary = "Obtener todas las alertas", description = "Devuelve todas las alertas asociadas a un usuario por su email")
	@ApiResponse(responseCode = "200", description = "Lista de alertas obtenida correctamente")
	@GetMapping("/list")
	public ResponseEntity<List<ResponseAlertDTO>> getAllAlerts() {
		return ResponseEntity.ok(alertService.getAllAlerts());
	}

	@Operation(summary = "Obtener una alerta por ID", description = "Devuelve una alerta específica del usuario")
	@ApiResponse(responseCode = "200", description = "Alerta encontrada")
	@ApiResponse(responseCode = "404", description = "Alerta no encontrada",
			content = @Content(schema = @Schema(implementation = ApiError.class)))
	@GetMapping("/{id}")
	public ResponseEntity<ResponseAlertDTO> getAlertById(@PathVariable Long id) {
		return ResponseEntity.ok(alertService.getAlertById(id));
	}

	@Operation(summary = "Obtener alertas por tipo", description = "Obtiene alertas de tipo ALERTA_MEDIA o ALERTA_ROJA")
	@ApiResponse(responseCode = "200", description = "Lista de alertas obtenida correctamente")
	@ApiResponse(responseCode = "404", description = "No hay alertas encontradas",
			content = @Content(schema = @Schema(implementation = ApiError.class)))
	@GetMapping("/type")
	public ResponseEntity<List<ResponseAlertDTO>> getAlertsByType(
			@RequestParam AlertType type) {
		return ResponseEntity.ok(alertService.getAlertsByType(type));
	}

	@Operation(summary = "Crear una nueva alerta", description = "Permite a un usuario crear una nueva alerta")
	@ApiResponse(responseCode = "201", description = "Alerta creada correctamente")
	@ApiResponse(responseCode = "400", description = "Datos inválidos",
			content = @Content(schema = @Schema(implementation = ApiError.class)))
	@PostMapping
	public ResponseEntity<ResponseAlertDTO> createAlert(
			@Valid @RequestBody RequestAlertDTO requestAlertDTO) {
		ResponseAlertDTO createdAlert = alertService.createAlert(requestAlertDTO);
		return ResponseEntity.status(HttpStatus.CREATED).body(createdAlert);
	}

	@Operation(summary = "Actualizar una alerta", description = "Actualiza los datos de una alerta específica del usuario")
	@ApiResponse(responseCode = "200", description = "Alerta actualizada correctamente",
			content = @Content(schema = @Schema(implementation = ResponseAlertDTO.class)))
	@ApiResponse(responseCode = "404", description = "Alerta no encontrada",
			content = @Content(schema = @Schema(implementation = ApiError.class)))
	@PutMapping("/{id}")
	public ResponseEntity<ResponseAlertDTO> updateAlertById(
			@PathVariable Long id,
			@Valid @RequestBody RequestAlertDTO requestAlertDTO) {
		ResponseAlertDTO updatedAlert = alertService.updateAlertById(id, requestAlertDTO);
		return ResponseEntity.ok(updatedAlert);
	}

	@Operation(summary = "Eliminar una alerta", description = "Elimina una alerta específica del usuario")
	@ApiResponse(responseCode = "204", description = "Alerta eliminada correctamente")
	@ApiResponse(responseCode = "404", description = "Alerta no encontrada",
			content = @Content(schema = @Schema(implementation = ApiError.class)))
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteAlertById(@PathVariable Long id) {
		alertService.deleteAlertById(id);
		return ResponseEntity.noContent().build();
	}


}
