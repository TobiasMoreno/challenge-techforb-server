package com.techforb.challenge_server.controllers;

import com.techforb.challenge_server.common.dto.ApiError;
import com.techforb.challenge_server.dtos.plant.RequestPlantDTO;
import com.techforb.challenge_server.dtos.plant.ResponsePlantDTO;
import com.techforb.challenge_server.services.PlantService;
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
@RequestMapping("api/plants")
@RequiredArgsConstructor
public class PlantController {

	private final PlantService plantService;

	@Operation(summary = "Obtener todas las plantas", description = "Devuelve todas las plantas asociadas a un usuario por su email")
	@ApiResponse(responseCode = "200", description = "Lista de plantas obtenida correctamente")
	@GetMapping("/list")
	public ResponseEntity<List<ResponsePlantDTO>> getAllPlants(@RequestParam String userEmail) {
		return ResponseEntity.ok(plantService.getAllPlants(userEmail));
	}

	@Operation(summary = "Obtener una planta por ID", description = "Devuelve una planta específica del usuario")
	@ApiResponse(responseCode = "200", description = "Planta encontrada")
	@ApiResponse(responseCode = "404", description = "Planta no encontrada",
			content = @Content(schema = @Schema(implementation = ApiError.class)))
	@GetMapping("/{id}")
	public ResponseEntity<ResponsePlantDTO> getPlantById(@PathVariable Long id, @RequestParam String userEmail) {
		return ResponseEntity.ok(plantService.getPlantById(id, userEmail));
	}

	@Operation(summary = "Crear una nueva planta", description = "Permite a un usuario crear una nueva planta")
	@ApiResponse(responseCode = "201", description = "Planta creada correctamente")
	@ApiResponse(responseCode = "400", description = "Datos inválidos",
			content = @Content(schema = @Schema(implementation = ApiError.class)))
	@PostMapping
	public ResponseEntity<ResponsePlantDTO> createPlant(
			@Valid @RequestBody RequestPlantDTO requestPlantDTO,
			@RequestParam String userEmail) {
		ResponsePlantDTO createdPlant = plantService.createPlant(requestPlantDTO, userEmail);
		return ResponseEntity.status(HttpStatus.CREATED).body(createdPlant);
	}

	@Operation(summary = "Actualizar una planta", description = "Actualiza los datos de una planta específica del usuario")
	@ApiResponse(responseCode = "200", description = "Planta actualizada correctamente",
			content = @Content(schema = @Schema(implementation = ResponsePlantDTO.class)))
	@ApiResponse(responseCode = "404", description = "Planta no encontrada",
			content = @Content(schema = @Schema(implementation = ApiError.class)))
	@PutMapping("/{id}")
	public ResponseEntity<ResponsePlantDTO> updatePlantById(
			@PathVariable Long id,
			@Valid @RequestBody RequestPlantDTO requestPlantDTO,
			@RequestParam String userEmail) {
		ResponsePlantDTO updatedPlant = plantService.updatePlantById(id, requestPlantDTO, userEmail);
		return ResponseEntity.ok(updatedPlant);
	}

	@Operation(summary = "Eliminar una planta", description = "Elimina una planta específica del usuario")
	@ApiResponse(responseCode = "204", description = "Planta eliminada correctamente")
	@ApiResponse(responseCode = "404", description = "Planta no encontrada",
			content = @Content(schema = @Schema(implementation = ApiError.class)))
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deletePlantById(@PathVariable Long id, @RequestParam String userEmail) {
		plantService.deletePlantById(id, userEmail);
		return ResponseEntity.noContent().build();
	}
}
