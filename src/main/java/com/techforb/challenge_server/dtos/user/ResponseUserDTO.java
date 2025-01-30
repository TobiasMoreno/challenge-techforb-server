package com.techforb.challenge_server.dtos.user;

import com.techforb.challenge_server.dtos.plant.ResponsePlantDTO;
import com.techforb.challenge_server.models.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseUserDTO {
	private Long id;
	private String email;
	private String password;
	private List<Role> roles;
	private List<ResponsePlantDTO> plants;
}
