package com.techforb.challenge_server.services.impl;

import com.techforb.challenge_server.entities.TokenEntity;
import com.techforb.challenge_server.repositories.TokenRepository;
import com.techforb.challenge_server.services.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

import static org.mockito.Mockito.*;

class AuthLogoutServiceTest {

	@Mock
	private JwtService jwtService;

	@Mock
	private TokenRepository tokenRepository;

	@Mock
	private HttpServletRequest request;

	@Mock
	private HttpServletResponse response;

	@Mock
	private Authentication authentication;

	@InjectMocks
	private AuthLogoutService authLogoutService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void logout_WhenTokenExists_ShouldRevokeAndClearContext() {
		// Arrange
		String accessToken = "valid-token";
		TokenEntity tokenEntity = new TokenEntity();
		tokenEntity.setExpired(false);
		tokenEntity.setRevoked(false);

		when(jwtService.parseJwt(request)).thenReturn(accessToken);
		when(tokenRepository.findByToken(accessToken)).thenReturn(Optional.of(tokenEntity));

		// Act
		authLogoutService.logout(request, response, authentication);

		// Assert
		verify(tokenRepository, times(1)).save(tokenEntity);
		verify(tokenRepository, times(1)).findByToken(accessToken);
		verify(jwtService, times(1)).parseJwt(request);
		assert tokenEntity.isExpired();
		assert tokenEntity.isRevoked();
		assert SecurityContextHolder.getContext().getAuthentication() == null;
	}

	@Test
	void logout_WhenTokenDoesNotExist_ShouldDoNothing() {
		// Arrange
		String accessToken = "invalid-token";
		when(jwtService.parseJwt(request)).thenReturn(accessToken);
		when(tokenRepository.findByToken(accessToken)).thenReturn(Optional.empty());

		// Act
		authLogoutService.logout(request, response, authentication);

		// Assert
		verify(tokenRepository, never()).save(any());
		verify(jwtService, times(1)).parseJwt(request);
		verify(tokenRepository, times(1)).findByToken(accessToken);
	}
}
