package com.techforb.challenge_server.dtos.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginDTO {
	@NotBlank(message = "El email es obligatorio.")
	@Email(message = "El email debe tener un formato válido.")
	private String email;

	@NotBlank(message = "La contraseña es obligatoria.")
	private String password;
}
