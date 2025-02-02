package com.techforb.challenge_server.repositories;

import com.techforb.challenge_server.entities.AlertEntity;
import com.techforb.challenge_server.entities.ReadingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlertRepository extends JpaRepository<AlertEntity,Long> {

	List<AlertEntity> findAllByReading(ReadingEntity reading);
}
