package com.techforb.challenge_server.controllers;

import com.techforb.challenge_server.services.AlertService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/alerts")
@RequiredArgsConstructor
public class AlertController {

	private final AlertService alertService;
}
