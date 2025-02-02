package com.techforb.challenge_server.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.techforb.challenge_server.dtos.auth.AuthResponse;
import com.techforb.challenge_server.dtos.auth.LoginRequest;
import com.techforb.challenge_server.dtos.auth.RegisterRequest;
import com.techforb.challenge_server.services.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private AuthService authService;

	private ObjectMapper objectMapper;

	@BeforeEach
	void setUp() {
		objectMapper = new ObjectMapper();
	}

	@Test
	void registerUser_ShouldReturnAuthResponse() throws Exception {
		// Arrange
		RegisterRequest registerRequest = new RegisterRequest("test@example.com", "password123");
		AuthResponse authResponse = new AuthResponse("accessToken123", "refreshToken123");

		when(authService.registerUser(Mockito.any(RegisterRequest.class))).thenReturn(authResponse);

		// Act & Assert
		mockMvc.perform(post("/api/auth/register")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(registerRequest)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.access_token").value("accessToken123"))
				.andExpect(jsonPath("$.refresh_token").value("refreshToken123"));

		verify(authService, times(1)).registerUser(any(RegisterRequest.class));
	}

	@Test
	void authenticateUser_ShouldReturnAuthResponse() throws Exception {
		// Arrange
		LoginRequest loginRequest = new LoginRequest("test@example.com", "password123");
		AuthResponse authResponse = new AuthResponse("accessToken123", "refreshToken123");

		when(authService.authenticateUser(Mockito.any(LoginRequest.class))).thenReturn(authResponse);

		// Act & Assert
		mockMvc.perform(post("/api/auth/login")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(loginRequest)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.access_token").value("accessToken123"))
				.andExpect(jsonPath("$.refresh_token").value("refreshToken123"));

		verify(authService, times(1)).authenticateUser(any(LoginRequest.class));
	}

	@Test
	void refreshToken_ShouldCallAuthService() throws Exception {
		// Arrange
		doNothing().when(authService).refreshToken(any(HttpServletRequest.class), any(HttpServletResponse.class));

		// Act & Assert
		mockMvc.perform(post("/api/auth/refresh-token"))
				.andExpect(status().isOk());

		verify(authService, times(1)).refreshToken(any(HttpServletRequest.class), any(HttpServletResponse.class));
	}
}
