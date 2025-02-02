package com.techforb.challenge_server.repositories;

import com.techforb.challenge_server.entities.ReadingEntity;
import jakarta.validation.constraints.Email;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReadingRepository extends JpaRepository<ReadingEntity, Long>{
	List<ReadingEntity> findAllByUser_Email(@Email String userEmail);
}
