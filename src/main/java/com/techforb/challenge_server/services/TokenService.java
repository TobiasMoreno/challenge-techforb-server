package com.techforb.challenge_server.services;

import com.techforb.challenge_server.models.User;
import org.springframework.stereotype.Service;

@Service
public interface TokenService {
    void saveUserToken(User user, String accessToken);

    void revokeAllUserTokens(Long userId);
}
