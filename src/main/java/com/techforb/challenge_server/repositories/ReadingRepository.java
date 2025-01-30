package com.techforb.challenge_server.repositories;

import com.techforb.challenge_server.entities.ReadingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReadingRepository extends JpaRepository<ReadingEntity, Long>{
}
