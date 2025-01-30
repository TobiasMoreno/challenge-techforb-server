package com.techforb.challenge_server.dtos.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRegistrationDTO {
	@NotBlank(message = "El email es obligatorio.")
	@Email(message = "El email debe tener un formato válido.")
	private String email;

	@NotBlank(message = "La contraseña es obligatoria.")
	@Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres.")
	private String password;

	@NotBlank(message = "La contraseña es obligatoria.")
	@Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres.")
	private String confirmPassword;
}
