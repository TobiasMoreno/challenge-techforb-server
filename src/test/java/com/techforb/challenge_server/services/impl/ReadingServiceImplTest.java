package com.techforb.challenge_server.services.impl;

import com.techforb.challenge_server.common.mapper.ModelMapperUtils;
import com.techforb.challenge_server.dtos.reading.RequestReadingDTO;
import com.techforb.challenge_server.dtos.reading.RequestUpdateReadingDTO;
import com.techforb.challenge_server.dtos.reading.ResponseReadingDTO;
import com.techforb.challenge_server.entities.ReadingEntity;
import com.techforb.challenge_server.entities.SensorEntity;
import com.techforb.challenge_server.entities.UserEntity;
import com.techforb.challenge_server.models.Role;
import com.techforb.challenge_server.repositories.ReadingRepository;
import com.techforb.challenge_server.repositories.SensorRepository;
import com.techforb.challenge_server.repositories.UserRepository;
import com.techforb.challenge_server.services.ReadingService;
import com.techforb.challenge_server.services.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@Transactional
class ReadingServiceImplTest {

	@Autowired
	private ReadingService readingService;

	@Autowired
	private UserService userService;

	@MockBean
	private ReadingRepository readingRepository;

	@MockBean
	private SensorRepository sensorRepository;

	@MockBean
	private UserRepository userRepository;

	@Autowired
	private ModelMapperUtils modelMapperUtils;

	private UserEntity testUser;
	private ReadingEntity testReading;


	@BeforeEach
	void setUp() {
		testUser = new UserEntity();
		testUser.setId(1L);
		testUser.setEmail("test@example.com");
		testUser.setPassword("password");
		testUser.setRole(Role.USER);
		userRepository.save(testUser);

		SensorEntity testSensor = new SensorEntity();
		testSensor.setId(1L);
		testSensor.setType("Temperature");
		testSensor.setAvailable(true);
		testSensor.setUser(testUser);
		sensorRepository.save(testSensor);

		testReading = new ReadingEntity();
		testReading.setId(1L);
		testReading.setUser(testUser);
		testReading.setSensor(testSensor);
		testReading.setReadingValue(22.5);
		testReading.setTimestamp(LocalDateTime.now());
		readingRepository.save(testReading);

		Authentication authentication = mock(Authentication.class);
		when(authentication.getName()).thenReturn(testUser.getEmail());

		SecurityContext securityContext = mock(SecurityContext.class);
		when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
	}

	@Test
	void getReadings_ShouldReturnReadings() {
		when(readingRepository.findAllByUser_Email("test@example.com"))
				.thenReturn(Collections.singletonList(testReading));

		List<ResponseReadingDTO> readings = readingService.getReadings("test@example.com");

		assertNotNull(readings);
		assertFalse(readings.isEmpty());

		verify(readingRepository, times(1)).findAllByUser_Email("test@example.com");
	}

	@Test
	void getReadings_WhenNoReadings_ShouldThrowException() {
		when(readingRepository.findAllByUser_Email("test@example.com"))
				.thenReturn(Collections.emptyList());

		Exception exception = assertThrows(IllegalArgumentException.class, () ->
				readingService.getReadings("test@example.com"));

		assertEquals("No se encontraron lecturas", exception.getMessage());
	}

	@Test
	void getReadingById_ShouldReturnReading() {
		when(readingRepository.findById(testReading.getId())).thenReturn(Optional.of(testReading));

		ResponseReadingDTO reading = readingService.getReadingById(testReading.getId(), "test@example.com");

		assertNotNull(reading);
		assertEquals(testReading.getId(), reading.getId());
	}

	@Test
	void getReadingById_ShouldReturnNotFound() {
		when(readingRepository.findById(testReading.getId())).thenReturn(Optional.empty());

		Exception exception = assertThrows(EntityNotFoundException.class, () ->
				readingService.getReadingById(testReading.getId(), "test@example.com"));

		assertEquals("No se encontraron lecturas con ese id", exception.getMessage());
	}

	@Test
	void getReadingById_ShouldReturnNotAuthorized() {
		when(readingRepository.findById(testReading.getId())).thenReturn(Optional.of(testReading));

		testUser.setEmail("test2@example.com");

		Exception exception = assertThrows(IllegalArgumentException.class, () ->
				readingService.getReadingById(testReading.getId(), "test@example.com"));

		assertEquals("Acceso no Autorizado", exception.getMessage());
	}

	@Test
	void createReading_ShouldReturnCreatedReading() {
		when(userRepository.findByEmail(testUser.getEmail()))
				.thenReturn(Optional.of(testUser));
		when(userService.getCurrentUserEntity()).thenReturn(testUser);
		when(readingRepository.save(testReading)).thenReturn(testReading);

		RequestReadingDTO request = new RequestReadingDTO();
		ResponseReadingDTO createdReading = readingService.createReading(request, "test@example.com");

		assertNotNull(createdReading);
		assertNotNull(createdReading.getId());
	}

	@Test
	void updateReading_ShouldUpdateReading_WhenValidDataProvided() {
		Long readingId = 1L;
		RequestUpdateReadingDTO updateDTO = new RequestUpdateReadingDTO();
		updateDTO.setReadingValue(25.0);
		updateDTO.setAlerts(new ArrayList<>());

		ReadingEntity existingReading = new ReadingEntity();
		existingReading.setId(readingId);
		existingReading.setReadingValue(22.5);
		existingReading.setUser(testUser);

		when(readingRepository.findById(readingId)).thenReturn(Optional.of(existingReading));

		ResponseReadingDTO updatedReading = readingService.updateReading(readingId, updateDTO, testUser.getEmail());

		assertNotNull(updatedReading);
		assertEquals(25.0, updatedReading.getReadingValue());
	}

	@Test
	void updateReading_ShouldThrowEntityNotFound_WhenReadingNotFound() {
		Long readingId = 1L;
		RequestUpdateReadingDTO updateDTO = new RequestUpdateReadingDTO();
		when(readingRepository.findById(readingId)).thenReturn(Optional.empty());

		EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
			readingService.updateReading(readingId, updateDTO, testUser.getEmail());
		});
		assertEquals("No se encontraron lecturas con ese id", exception.getMessage());
	}

	@Test
	void updateReading_ShouldThrowIllegalArgument_WhenUnauthorizedAccess() {
		Long readingId = 1L;
		RequestUpdateReadingDTO updateDTO = new RequestUpdateReadingDTO();
		updateDTO.setReadingValue(25.0);

		ReadingEntity existingReading = new ReadingEntity();
		existingReading.setId(readingId);
		existingReading.setReadingValue(22.5);
		existingReading.setUser(testUser);

		when(readingRepository.findById(readingId)).thenReturn(Optional.of(existingReading));

		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
			readingService.updateReading(readingId, updateDTO, "unauthorized@example.com");
		});
		assertEquals("Acceso no Autorizado", exception.getMessage());
	}

	@Test
	void deleteReading_ShouldDeleteReading_WhenValidDataProvided() {
		Long readingId = 1L;
		ReadingEntity existingReading = new ReadingEntity();
		existingReading.setId(readingId);
		existingReading.setReadingValue(22.5);
		existingReading.setUser(testUser);

		when(readingRepository.findById(readingId)).thenReturn(Optional.of(existingReading));
		doNothing().when(readingRepository).delete(existingReading);

		readingService.deleteReading(readingId, testUser.getEmail());

		verify(readingRepository, times(1)).delete(existingReading);
	}

	@Test
	void deleteReading_ShouldThrowEntityNotFound_WhenReadingNotFound() {
		Long readingId = 1L;
		when(readingRepository.findById(readingId)).thenReturn(Optional.empty());

		EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
			readingService.deleteReading(readingId, testUser.getEmail());
		});
		assertEquals("No se encontraron lecturas con ese id", exception.getMessage());
	}

	@Test
	void deleteReading_ShouldThrowIllegalArgument_WhenUnauthorizedAccess() {
		Long readingId = 1L;
		ReadingEntity existingReading = new ReadingEntity();
		existingReading.setId(readingId);
		existingReading.setReadingValue(22.5);
		existingReading.setUser(testUser);

		when(readingRepository.findById(readingId)).thenReturn(Optional.of(existingReading));

		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
			readingService.deleteReading(readingId, "unauthorized@example.com");
		});
		assertEquals("Acceso no Autorizado", exception.getMessage());
	}

}