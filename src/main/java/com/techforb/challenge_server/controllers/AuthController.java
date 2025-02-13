package com.techforb.challenge_server.controllers;

import com.techforb.challenge_server.dtos.auth.AuthResponse;
import com.techforb.challenge_server.dtos.auth.LoginRequest;
import com.techforb.challenge_server.dtos.auth.RegisterRequest;
import com.techforb.challenge_server.services.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
public class AuthController {

	private final AuthService authService;

	@PostMapping("/register")
	public ResponseEntity<AuthResponse> registerUser(@RequestBody RegisterRequest registerRequest) {
		return ResponseEntity.ok(authService.registerUser(registerRequest));
	}

	@PostMapping("/login")
	public ResponseEntity<AuthResponse> authenticateUser(@RequestBody LoginRequest loginRequest) {
		return ResponseEntity.ok(authService.authenticateUser(loginRequest));
	}

	@PostMapping("/refresh-token")
	public ResponseEntity<AuthResponse> refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
		return authService.refreshToken(request, response);
	}
}
