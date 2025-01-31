package com.techforb.challenge_server.services.impl;

import com.techforb.challenge_server.common.mapper.ModelMapperUtils;
import com.techforb.challenge_server.entities.TokenEntity;
import com.techforb.challenge_server.entities.UserEntity;
import com.techforb.challenge_server.models.TokenType;
import com.techforb.challenge_server.models.User;
import com.techforb.challenge_server.repositories.TokenRepository;
import com.techforb.challenge_server.services.TokenService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@SpringBootTest
@Transactional
class TokenServiceImplTest {

	@Autowired
	private TokenService tokenService;

	@MockBean
	private TokenRepository tokenRepository;  // Simula el repositorio

	@MockBean
	private ModelMapperUtils modelMapperUtils; // Simula el ModelMapper

	private User user;
	private UserEntity userEntity;
	private String testToken;

	@BeforeEach
	void setUp() {
		user = new User();
		user.setId(1L);
		user.setEmail("test@example.com");

		userEntity = new UserEntity();
		userEntity.setId(1L);
		userEntity.setEmail("test@example.com");

		testToken = "test-access-token";
	}

	@Test
	void saveUserToken_ShouldSaveToken() {
		when(modelMapperUtils.map(user, UserEntity.class)).thenReturn(userEntity);

		tokenService.saveUserToken(user, testToken);

		verify(tokenRepository, times(1)).save(any(TokenEntity.class));
	}

	@Test
	void revokeAllUserTokens_ShouldRevokeTokens() {
		List<TokenEntity> tokens = List.of(
				new TokenEntity(1L, "token1", TokenType.BEARER, false, false, userEntity),
				new TokenEntity(2L, "token2", TokenType.BEARER, false, false, userEntity)
		);

		when(tokenRepository.findAllValidTokenByUserId(1L)).thenReturn(tokens);

		tokenService.revokeAllUserTokens(1L);

		for (TokenEntity token : tokens) {
			assertTrue(token.isExpired());
			assertTrue(token.isRevoked());
		}

		verify(tokenRepository, times(1)).saveAll(tokens);
	}

	@Test
	void revokeAllUserTokens_ShouldNotRevokeIfNoTokensFound() {
		when(tokenRepository.findAllValidTokenByUserId(1L)).thenReturn(List.of());

		tokenService.revokeAllUserTokens(1L);

		verify(tokenRepository, never()).saveAll(anyList());
	}
}