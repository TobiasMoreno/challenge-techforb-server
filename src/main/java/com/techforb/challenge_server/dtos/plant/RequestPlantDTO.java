package com.techforb.challenge_server.dtos.plant;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestPlantDTO {

	@NotBlank(message = "El nombre de la planta es obligatorio")
	private String name;

	@NotBlank(message = "El pais de la planta es obligatorio")
	private String country;

}
