package com.techforb.challenge_server.dtos.plant;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RequestPlantDTO {

	@NotBlank(message = "El nombre de la planta es obligatorio")
	private String name;

	private String country;

}
