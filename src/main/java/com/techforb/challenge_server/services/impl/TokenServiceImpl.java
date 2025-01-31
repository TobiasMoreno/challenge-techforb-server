package com.techforb.challenge_server.services.impl;

import com.techforb.challenge_server.common.mapper.ModelMapperUtils;
import com.techforb.challenge_server.entities.TokenEntity;
import com.techforb.challenge_server.entities.UserEntity;
import com.techforb.challenge_server.models.TokenType;
import com.techforb.challenge_server.models.User;
import com.techforb.challenge_server.repositories.TokenRepository;
import com.techforb.challenge_server.services.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {
    private final TokenRepository tokenRepository;
    private final ModelMapperUtils modelMapperUtils;

    @Override
    public void saveUserToken(User user, String accessToken) {
        UserEntity userEntity = modelMapperUtils.map(user, UserEntity.class);

        TokenEntity token = TokenEntity.builder()
                .user(userEntity)
                .token(accessToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();

        tokenRepository.save(token);
    }

    @Override
    public void revokeAllUserTokens(Long userId) {
        List<TokenEntity> validUserTokenList = tokenRepository.findAllValidTokenByUserId(userId);

        if (validUserTokenList.isEmpty()) {
            return;
        }

        validUserTokenList.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });

        tokenRepository.saveAll(validUserTokenList);
    }
}
