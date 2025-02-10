package com.techforb.challenge_server.dtos.alert;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseAlertCount {
	private Integer readingsOk;
	private Integer mediumAlerts;
	private Integer redAlerts;
}
