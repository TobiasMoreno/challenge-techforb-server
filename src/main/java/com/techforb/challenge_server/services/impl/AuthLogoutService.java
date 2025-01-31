package com.techforb.challenge_server.services.impl;

import com.techforb.challenge_server.entities.TokenEntity;
import com.techforb.challenge_server.repositories.TokenRepository;
import com.techforb.challenge_server.services.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthLogoutService implements LogoutHandler {

	private final JwtService jwtService;
	private final TokenRepository tokenRepository;

	@Override
	public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
		String accessToken = jwtService.parseJwt(request);

		TokenEntity storedToken = tokenRepository.findByToken(accessToken).orElse(null);

		if (storedToken != null) {
			storedToken.setExpired(true);
			storedToken.setRevoked(true);
			tokenRepository.save(storedToken);
			SecurityContextHolder.clearContext();
		}
	}
}
