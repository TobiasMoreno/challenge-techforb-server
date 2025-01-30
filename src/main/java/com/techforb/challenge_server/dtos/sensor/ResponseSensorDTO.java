package com.techforb.challenge_server.dtos.sensor;

import com.techforb.challenge_server.dtos.plant.ResponsePlantDTO;
import com.techforb.challenge_server.dtos.reading.ResponseReadingDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseSensorDTO {
	private Long id;
	private String type;
	private boolean isAvailable;
	private ResponsePlantDTO plant;
	private List<ResponseReadingDTO> readings;
}
