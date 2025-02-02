package com.techforb.challenge_server.repositories;

import com.techforb.challenge_server.entities.PlantEntity;
import jakarta.validation.constraints.Email;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlantRepository extends JpaRepository<PlantEntity,Long> {
	List<PlantEntity> findAllByOwner_Email(@Email String ownerEmail);

	Optional<PlantEntity> findByIdAndOwner_Email(Long id, @Email String ownerEmail);
}
