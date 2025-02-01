package com.techforb.challenge_server.services;

import com.techforb.challenge_server.dtos.reading.RequestReadingDTO;
import com.techforb.challenge_server.dtos.reading.RequestUpdateReadingDTO;
import com.techforb.challenge_server.dtos.reading.ResponseReadingDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ReadingService {
	List<ResponseReadingDTO> getReadings(String currentUserEmail);
	ResponseReadingDTO getReadingById(Long id,String currentUserEmail);
	ResponseReadingDTO createReading(RequestReadingDTO readingDTO,String currentUserEmail);
	ResponseReadingDTO updateReading(Long id, RequestUpdateReadingDTO readingDTO,String currentUserEmail);
	void deleteReading(Long id,String currentUserEmail);
}
