package com.techforb.challenge_server.dtos.sensor;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseSensorStatsDTO {
	private Long sensorId;
	private String sensorType;
	private int readingsOk;
	private int mediumAlerts;
	private int redAlerts;
}
