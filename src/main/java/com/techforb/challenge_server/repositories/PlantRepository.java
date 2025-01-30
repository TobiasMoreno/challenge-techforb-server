package com.techforb.challenge_server.repositories;

import com.techforb.challenge_server.entities.PlantEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlantRepository extends JpaRepository<PlantEntity,Long> {
}
