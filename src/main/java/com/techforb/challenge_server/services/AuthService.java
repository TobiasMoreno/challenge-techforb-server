package com.techforb.challenge_server.services;

import com.techforb.challenge_server.dtos.auth.AuthResponse;
import com.techforb.challenge_server.dtos.auth.LoginRequest;
import com.techforb.challenge_server.dtos.auth.RegisterRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public interface AuthService {
	AuthResponse registerUser(RegisterRequest registerRequest);

	ResponseEntity<AuthResponse> refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException;

	AuthResponse authenticateUser(LoginRequest loginRequest);
}
