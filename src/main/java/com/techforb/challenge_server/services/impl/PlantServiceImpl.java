package com.techforb.challenge_server.services.impl;

import com.techforb.challenge_server.repositories.PlantRepository;
import com.techforb.challenge_server.services.PlantService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlantServiceImpl implements PlantService {

	private final PlantRepository plantRepository;
}
