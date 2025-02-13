package com.techforb.challenge_server.services.impl;

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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

		if (registerRequest == null || registerRequest.getEmail() == null || registerRequest.getPassword() == null) {
			throw new IllegalArgumentException("Email y contraseña son requeridos");
		}

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
	public ResponseEntity<AuthResponse> refreshToken(HttpServletRequest request, HttpServletResponse response) {
		String refreshToken = jwtService.parseJwt(request);

		if (Objects.isNull(refreshToken)) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}

		String userEmail = jwtService.getUsernameFromToken(refreshToken);

		if (Objects.isNull(userEmail)) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
		}

		UserEntity userEntity = userRepository.findByEmail(userEmail)
				.orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

		if (!jwtService.validateToken(refreshToken, userEntity)) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
		}

		String accessToken = jwtService.generateToken(userEntity);
		tokenService.revokeAllUserTokens(userEntity.getId());
		tokenService.saveUserToken(modelMapperUtils.map(userEntity, User.class), accessToken);

		AuthResponse authResponse = AuthResponse.builder()
				.accessToken(accessToken)
				.refreshToken(refreshToken)
				.build();

		return ResponseEntity.ok(authResponse);
	}
}
