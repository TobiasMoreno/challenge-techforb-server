package com.techforb.challenge_server.services;

import com.techforb.challenge_server.dtos.plant.RequestPlantDTO;
import com.techforb.challenge_server.dtos.plant.ResponseCountPlantDTO;
import com.techforb.challenge_server.dtos.plant.ResponsePlantDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PlantService {
	List<ResponsePlantDTO> getAllPlants();
	ResponsePlantDTO getPlantById(Long id);
	ResponsePlantDTO createPlant(RequestPlantDTO requestPlantDTO);
	ResponsePlantDTO updatePlantById(Long id, RequestPlantDTO requestPlantDTO);
	void deletePlantById(Long id);
	List<ResponseCountPlantDTO> getCountPlants();
}
