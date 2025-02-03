package com.techforb.challenge_server.repositories;

import com.techforb.challenge_server.entities.SensorEntity;
import jakarta.validation.constraints.Email;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SensorRepository extends JpaRepository<SensorEntity, Long> {
	List<SensorEntity> findByIsAvailableFalseAndUser_Email(@Email String email);
}