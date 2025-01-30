package com.techforb.challenge_server.services.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.techforb.challenge_server.common.mapper.ModelMapperUtils;
import com.techforb.challenge_server.dtos.auth.AuthResponse;
import com.techforb.challenge_server.dtos.auth.LoginRequest;
import com.techforb.challenge_server.dtos.auth.RegisterRequest;
import com.techforb.challenge_server.entities.UserEntity;
import com.techforb.challenge_server.models.Role;
import com.techforb.challenge_server.models.User;
import com.techforb.challenge_server.repositories.UserRepository;
import com.techforb.challenge_server.services.AuthService;
import com.techforb.challenge_server.services.JwtService;
import com.techforb.challenge_server.services.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final AuthenticationManager authenticationManager;
	private final JwtService jwtService;
	private final TokenService tokenService;
	private final ModelMapperUtils modelMapperUtils;

	@Override
	public AuthResponse registerUser(RegisterRequest registerRequest) {
		if (userRepository.existsByEmail(registerRequest.getEmail())) {
			throw new IllegalStateException("El email ya se encuentra registrado");
		}

		UserEntity userEntity = UserEntity.builder()
				.email(registerRequest.getEmail())
				.password(passwordEncoder.encode(registerRequest.getPassword()))
				.role(Role.USER)
				.build();

		UserEntity userSaved = userRepository.save(userEntity);

		String accessToken = jwtService.generateToken(userEntity);
		String refreshToken = jwtService.generateRefreshToken(userEntity);

		tokenService.saveUserToken(modelMapperUtils.map(userSaved, User.class), accessToken);

		return AuthResponse.builder()
				.accessToken(accessToken)
				.refreshToken(refreshToken)
				.build();
	}

	@Override
	public AuthResponse authenticateUser(LoginRequest loginRequest) {
		authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(
						loginRequest.getEmail(),
						loginRequest.getPassword()
				)
		);

		UserEntity userEntity = userRepository.findByEmail(loginRequest.getEmail()).orElseThrow();

		String accessToken = jwtService.generateToken(userEntity);
		String refreshToken = jwtService.generateRefreshToken(userEntity);

		tokenService.revokeAllUserTokens(userEntity.getId());
		tokenService.saveUserToken(modelMapperUtils.map(userEntity, User.class), accessToken);

		return AuthResponse.builder()
				.accessToken(accessToken)
				.refreshToken(refreshToken)
				.build();
	}

	@Override
	public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String refreshToken = jwtService.parseJwt(request);

		if (Objects.isNull(refreshToken)) {
			return;
		}

		String userEmail = jwtService.getUsernameFromToken(refreshToken);

		if (Objects.nonNull(userEmail)) {
			UserEntity userEntity = userRepository.findByEmail(userEmail).orElseThrow();

			if (jwtService.validateToken(refreshToken, userEntity)) {
				String accessToken = jwtService.generateToken(userEntity);

				tokenService.revokeAllUserTokens(userEntity.getId());
				tokenService.saveUserToken(modelMapperUtils.map(userEntity, User.class), accessToken);

				AuthResponse authResponse = AuthResponse.builder()
						.accessToken(accessToken)
						.refreshToken(refreshToken)
						.build();

				new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
			}
		}
	}
}
