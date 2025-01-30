package com.techforb.challenge_server.controllers;

import com.techforb.challenge_server.services.PlantService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/plants")
@RequiredArgsConstructor
public class PlantController {

	private final PlantService plantService;
}
