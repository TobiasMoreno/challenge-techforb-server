package com.techforb.challenge_server.services.impl;

import com.techforb.challenge_server.repositories.SensorRepository;
import com.techforb.challenge_server.services.SensorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SensorServiceImpl implements SensorService {

	private final SensorRepository sensorRepository;
}
