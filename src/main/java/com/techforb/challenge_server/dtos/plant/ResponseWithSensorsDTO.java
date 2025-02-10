package com.techforb.challenge_server.dtos.plant;


import com.techforb.challenge_server.dtos.sensor.ResponseSensorDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseWithSensorsDTO {
	private Long id;
	private String name;
	private String country;
	private List<ResponseSensorDTO> sensors;
}
