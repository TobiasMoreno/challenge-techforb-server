package com.techforb.challenge_server.repositories;

import com.techforb.challenge_server.entities.AlertEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlertRepository extends JpaRepository<AlertEntity,Long> {
}
