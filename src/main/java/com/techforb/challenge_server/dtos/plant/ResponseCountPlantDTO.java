package com.techforb.challenge_server.dtos.plant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseCountPlantDTO {
	private Long id;
	private String name;
	private String country;
	private Integer readingsOk;
	private Integer mediumAlerts;
	private Integer redAlerts;
}
