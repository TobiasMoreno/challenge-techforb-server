package com.techforb.challenge_server.services.impl;

import com.techforb.challenge_server.entities.TokenEntity;
import com.techforb.challenge_server.repositories.TokenRepository;
import com.techforb.challenge_server.services.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JwtAuthenticationFilterTest {

	@Mock
	private JwtService jwtService;

	@Mock
	private UserDetailsService userDetailsService;

	@Mock
	private TokenRepository tokenRepository;

	@Mock
	private HttpServletRequest request;

	@Mock
	private HttpServletResponse response;

	@Mock
	private FilterChain filterChain;

	private JwtAuthenticationFilter jwtAuthenticationFilter;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		jwtAuthenticationFilter = new JwtAuthenticationFilter(jwtService, userDetailsService, tokenRepository);
	}

	@Test
	void doFilterInternal_ValidToken_ShouldAuthenticateUser() throws ServletException, IOException {
		// Arrange
		String token = "validToken";
		String email = "test@example.com";

		UserDetails userDetails = new User(email, "password", Collections.emptyList());
		TokenEntity tokenEntity = new TokenEntity();
		tokenEntity.setExpired(false);
		tokenEntity.setRevoked(false);

		when(jwtService.parseJwt(request)).thenReturn(token);
		when(jwtService.getUsernameFromToken(token)).thenReturn(email);
		when(userDetailsService.loadUserByUsername(email)).thenReturn(userDetails);
		when(tokenRepository.findByToken(token)).thenReturn(Optional.of(tokenEntity));
		when(jwtService.validateToken(token, userDetails)).thenReturn(true);

		SecurityContext securityContext = mock(SecurityContext.class);
		SecurityContextHolder.setContext(securityContext);

		// Act
		jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

		// Assert
		ArgumentCaptor<UsernamePasswordAuthenticationToken> captor = ArgumentCaptor.forClass(UsernamePasswordAuthenticationToken.class);
		verify(securityContext).setAuthentication(captor.capture());

		UsernamePasswordAuthenticationToken authToken = captor.getValue();
		assertNotNull(authToken);
		assertEquals(userDetails, authToken.getPrincipal());

		verify(filterChain).doFilter(request, response);
	}

	@Test
	void doFilterInternal_InvalidToken_ShouldNotAuthenticateUser() throws ServletException, IOException {
		// Arrange
		String token = "invalidToken";
		when(jwtService.parseJwt(request)).thenReturn(token);
		when(jwtService.getUsernameFromToken(token)).thenReturn(null);

		// Act
		jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

		// Assert
		assertNull(SecurityContextHolder.getContext().getAuthentication());
		verify(filterChain).doFilter(request, response);
	}

	@Test
	void doFilterInternal_ExpiredToken_ShouldNotAuthenticateUser() throws ServletException, IOException {
		// Arrange
		String token = "expiredToken";
		String email = "test@example.com";

		UserDetails userDetails = new User(email, "password", Collections.emptyList());
		TokenEntity tokenEntity = new TokenEntity();
		tokenEntity.setExpired(true);
		tokenEntity.setRevoked(false);

		when(jwtService.parseJwt(request)).thenReturn(token);
		when(jwtService.getUsernameFromToken(token)).thenReturn(email);
		when(userDetailsService.loadUserByUsername(email)).thenReturn(userDetails);
		when(tokenRepository.findByToken(token)).thenReturn(Optional.of(tokenEntity));
		when(jwtService.validateToken(token, userDetails)).thenReturn(false);

		// Act
		jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

		// Assert
		assertNull(SecurityContextHolder.getContext().getAuthentication());
		verify(filterChain).doFilter(request, response);
	}
}
