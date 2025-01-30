package com.techforb.challenge_server.services.impl;

import com.techforb.challenge_server.repositories.AlertRepository;
import com.techforb.challenge_server.services.AlertService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AlertServiceImpl implements AlertService {

	private final AlertRepository alertRepository;
}
