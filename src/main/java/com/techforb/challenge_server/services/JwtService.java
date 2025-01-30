package com.techforb.challenge_server.services;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.function.Function;

@Service
public interface JwtService {
    String getUsernameFromToken(String token);

    <T> T getClaim(String token, Function<Claims, T> claimsResolver);

    String generateRefreshToken(UserDetails userDetails);

    String buildToken(Map<String, Object> extraClaims, UserDetails userDetails, long tokenExpiration);

    String generateToken(UserDetails userDetails);

    boolean validateToken(String token, UserDetails userDetails);

    String parseJwt(HttpServletRequest request);
}
