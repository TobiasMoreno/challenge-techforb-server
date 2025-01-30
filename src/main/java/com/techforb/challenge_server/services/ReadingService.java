package com.techforb.challenge_server.services;

import com.techforb.challenge_server.dtos.reading.ResponseReadingDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ReadingService {
	List<ResponseReadingDTO> getReadings();
}
