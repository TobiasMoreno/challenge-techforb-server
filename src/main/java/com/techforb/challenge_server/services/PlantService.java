package com.techforb.challenge_server.services;

import com.techforb.challenge_server.dtos.plant.RequestPlantDTO;
import com.techforb.challenge_server.dtos.plant.ResponsePlantDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PlantService {
	List<ResponsePlantDTO> getAllPlants(String userEmail);
	ResponsePlantDTO getPlantById(Long id, String userEmail);
	ResponsePlantDTO createPlant(RequestPlantDTO requestPlantDTO, String userEmail);
	ResponsePlantDTO updatePlantById(Long id, RequestPlantDTO requestPlantDTO, String userEmail);
	void deletePlantById(Long id, String userEmail);
}
