package com.techforb.challenge_server.repositories;

import com.techforb.challenge_server.entities.TokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<TokenEntity, Long> {
    @Query(value = """
            SELECT t FROM TokenEntity t
            JOIN UserEntity u ON t.user.id = u.id
            WHERE u.id = :id AND (t.expired = false OR t.revoked = false)
            """)
    List<TokenEntity> findAllValidTokenByUserId(Long id);

    Optional<TokenEntity> findByToken(String token);
}
