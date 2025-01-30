package com.techforb.challenge_server.repositories;

import com.techforb.challenge_server.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
	boolean existsByEmail(String email);

	Optional<UserEntity> findByEmail(String email);
}
