package com.techforb.challenge_server.controllers;

import com.techforb.challenge_server.services.SensorService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/sensors")
@RequiredArgsConstructor
public class SensorController {

	private final SensorService sensorService;
}
