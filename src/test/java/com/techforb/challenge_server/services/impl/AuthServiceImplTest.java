package com.techforb.challenge_server.services.impl;

import com.techforb.challenge_server.common.mapper.ModelMapperUtils;
import com.techforb.challenge_server.dtos.auth.AuthResponse;
import com.techforb.challenge_server.dtos.auth.LoginRequest;
import com.techforb.challenge_server.dtos.auth.RegisterRequest;
import com.techforb.challenge_server.entities.PlantEntity;
import com.techforb.challenge_server.entities.UserEntity;
import com.techforb.challenge_server.models.Role;
import com.techforb.challenge_server.models.User;
import com.techforb.challenge_server.repositories.UserRepository;
import com.techforb.challenge_server.services.AuthService;
import com.techforb.challenge_server.services.JwtService;
import com.techforb.challenge_server.services.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.PrintWriter;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@Transactional
class AuthServiceImplTest {

	@MockBean
	private UserRepository userRepository;

	@MockBean
	private PasswordEncoder passwordEncoder;

	@MockBean
	private AuthenticationManager authenticationManager;

	@MockBean
	private JwtService jwtService;

	@MockBean
	private TokenService tokenService;

	@MockBean
	private ModelMapperUtils modelMapperUtils;

	private AuthServiceImpl authService;

	@BeforeEach
	void setUp() {
		authService = new AuthServiceImpl(
				userRepository, passwordEncoder, authenticationManager, jwtService, tokenService, modelMapperUtils);
	}

	@Test
	void registerUser_Success() {
		RegisterRequest request = new RegisterRequest("test@example.com", "password123");
		UserEntity userEntity = new UserEntity();
		userEntity.setEmail(request.getEmail());
		userEntity.setPassword("encodedPassword");

		User mappedUser = new User();
		mappedUser.setEmail(request.getEmail());

		when(userRepository.existsByEmail(request.getEmail())).thenReturn(false);
		when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedPassword");
		when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);
		when(jwtService.generateToken(any(UserEntity.class))).thenReturn("testAccessToken");
		when(jwtService.generateRefreshToken(any(UserEntity.class))).thenReturn("testRefreshToken");
		when(modelMapperUtils.map(any(UserEntity.class), eq(User.class))).thenReturn(mappedUser);

		AuthResponse response = authService.registerUser(request);

		assertNotNull(response);
		assertEquals("testAccessToken", response.getAccessToken());
		assertEquals("testRefreshToken", response.getRefreshToken());

		verify(tokenService).saveUserToken(mappedUser, "testAccessToken");
	}
	@Test
	void registerUser_ShouldThrowExceptionWhenEmailExists() {
		// Arrange
		RegisterRequest request = new RegisterRequest("test@example.com", "password123");
		when(userRepository.existsByEmail(request.getEmail())).thenReturn(true);

		// Act & Assert
		IllegalStateException exception = assertThrows(IllegalStateException.class, () -> authService.registerUser(request));
		assertEquals("El email ya se encuentra registrado", exception.getMessage());

		verify(userRepository, never()).save(any(UserEntity.class));
		verify(tokenService, never()).saveUserToken(any(User.class), anyString());
	}

	@Test
	void authenticateUser_ShouldAuthenticateSuccessfully() {
		// Arrange
		LoginRequest loginRequest = new LoginRequest("test@example.com", "password123");
		UserEntity userEntity = new UserEntity();
		userEntity.setId(1L);
		userEntity.setEmail(loginRequest.getEmail());

		User mappedUser = new User();
		mappedUser.setEmail(loginRequest.getEmail());

		when(userRepository.findByEmail(loginRequest.getEmail())).thenReturn(Optional.of(userEntity));
		when(jwtService.generateToken(userEntity)).thenReturn("testAccessToken");
		when(jwtService.generateRefreshToken(userEntity)).thenReturn("testRefreshToken");
		when(modelMapperUtils.map(any(UserEntity.class), eq(User.class))).thenReturn(mappedUser);

		// Act
		AuthResponse response = authService.authenticateUser(loginRequest);

		// Assert
		assertNotNull(response);
		assertEquals("testAccessToken", response.getAccessToken());
		assertEquals("testRefreshToken", response.getRefreshToken());

		// Verifica autenticación
		verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));

		// Verifica tokens
		verify(tokenService).revokeAllUserTokens(1L);
		verify(tokenService).saveUserToken(mappedUser, "testAccessToken");
	}

	@Test
	void authenticateUser_ShouldThrowExceptionWhenUserNotFound() {
		// Arrange
		LoginRequest loginRequest = new LoginRequest("notfound@example.com", "password123");
		when(userRepository.findByEmail(loginRequest.getEmail())).thenReturn(Optional.empty());

		// Act & Assert
		assertThrows(RuntimeException.class, () -> authService.authenticateUser(loginRequest));
	}

	@Test
	void refreshToken_ShouldGenerateNewAccessToken() throws Exception {
		// Arrange
		HttpServletRequest request = mock(HttpServletRequest.class);
		HttpServletResponse response = mock(HttpServletResponse.class);
		PrintWriter writer = mock(PrintWriter.class);

		when(response.getWriter()).thenReturn(writer);

		String refreshToken = "testRefreshToken";
		String email = "test@example.com";

		UserEntity userEntity = new UserEntity();
		userEntity.setId(1L);
		userEntity.setEmail(email);

		User mappedUser = new User();
		mappedUser.setEmail(email);

		when(jwtService.parseJwt(request)).thenReturn(refreshToken);
		when(jwtService.getUsernameFromToken(refreshToken)).thenReturn(email);
		when(userRepository.findByEmail(email)).thenReturn(Optional.of(userEntity));
		when(jwtService.validateToken(refreshToken, userEntity)).thenReturn(true);
		when(jwtService.generateToken(userEntity)).thenReturn("newAccessToken");
		doNothing().when(tokenService).revokeAllUserTokens(1L);
		doNothing().when(tokenService).saveUserToken(mappedUser, "newAccessToken");
		when(modelMapperUtils.map(userEntity, User.class)).thenReturn(mappedUser);

		// Act
		authService.refreshToken(request, response);

		// Assert
		verify(tokenService).revokeAllUserTokens(1L);
		verify(tokenService).saveUserToken(mappedUser, "newAccessToken");
		verify(response).getWriter();
	}
}