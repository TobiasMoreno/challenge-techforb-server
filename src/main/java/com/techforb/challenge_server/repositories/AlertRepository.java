package com.techforb.challenge_server.repositories;

import com.techforb.challenge_server.entities.AlertEntity;
import com.techforb.challenge_server.entities.ReadingEntity;
import com.techforb.challenge_server.models.AlertType;
import jakarta.validation.constraints.Email;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AlertRepository extends JpaRepository<AlertEntity, Long> {
	List<AlertEntity> findAllByUser_Email(@Email String email);

	Optional<AlertEntity> findByIdAndUser_Email(Long id, @Email String email);

	List<AlertEntity> findAllByTypeAndUser_Email(AlertType type, String userEmail);

	List<AlertEntity> findAllByReading_IdAndUser_Email(Long readingId, String userEmail);

	@Query("SELECT COUNT(a) FROM AlertEntity a WHERE a.type = 'MEDIA' AND a.sensor.plant.owner.email = :userEmail")
	long countMediumAlertsByUser_Email(@Email String userEmail);

	@Query("SELECT COUNT(a) FROM AlertEntity a WHERE a.type = 'ROJA' AND a.sensor.plant.owner.email = :userEmail")
	long countRedAlertsByUser_Email(@Email String userEmail);
}