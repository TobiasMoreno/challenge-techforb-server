package com.techforb.challenge_server.services.impl;

import com.techforb.challenge_server.common.mapper.ModelMapperUtils;
import com.techforb.challenge_server.dtos.user.ResponseUserDTO;
import com.techforb.challenge_server.entities.UserEntity;
import com.techforb.challenge_server.repositories.UserRepository;
import com.techforb.challenge_server.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	private final ModelMapperUtils modelMapperUtils;

	@Override
	public ResponseUserDTO getUserByEmail(String email) {
		return modelMapperUtils.map(userRepository.findByEmail(email).get(), ResponseUserDTO.class);
	}

	@Override
	public String getCurrentUserEmail() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null) {
			throw new RuntimeException("No hay contexto de seguridad disponible.");
		}
		if (!authentication.isAuthenticated() || authentication.getName() == null) {
			throw new RuntimeException("No se pudo obtener el correo del usuario, autenticación no válida.");
		}
		return authentication.getName();
	}


	@Override
	public UserEntity getCurrentUserEntity() {
		String email = getCurrentUserEmail();
		return userRepository.findByEmail(email)
				.orElseThrow(() -> new RuntimeException("Usuario no encontrado con el correo: " + email));
	}


}
