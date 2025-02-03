package com.techforb.challenge_server.services.impl;

import com.techforb.challenge_server.common.mapper.ModelMapperUtils;
import com.techforb.challenge_server.dtos.alert.RequestAlertDTO;
import com.techforb.challenge_server.dtos.alert.ResponseAlertDTO;
import com.techforb.challenge_server.entities.AlertEntity;
import com.techforb.challenge_server.entities.ReadingEntity;
import com.techforb.challenge_server.entities.UserEntity;
import com.techforb.challenge_server.models.AlertType;
import com.techforb.challenge_server.repositories.AlertRepository;
import com.techforb.challenge_server.repositories.ReadingRepository;
import com.techforb.challenge_server.repositories.UserRepository;
import com.techforb.challenge_server.services.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class AlertServiceImplTest {

	@MockBean
	private AlertRepository alertRepository;

	@MockBean
	private UserRepository userRepository;

	@MockBean
	private ReadingRepository readingRepository;

	@MockBean
	private ModelMapperUtils modelMapperUtils;

	@MockBean
	private UserService userService;

	@Autowired
	private AlertServiceImpl alertService;

	private UserEntity user;
	private AlertEntity alert;
	private RequestAlertDTO requestAlertDTO;
	private ResponseAlertDTO responseAlertDTO;

	@BeforeEach
	void setUp() {
		user = new UserEntity();
		user.setEmail("user@example.com");

		alert = new AlertEntity();
		alert.setId(1L);
		alert.setUser(user);
		alert.setType(AlertType.ALERTA_ROJA);
		alert.setMessage("Alerta Roja");

		requestAlertDTO = new RequestAlertDTO();
		requestAlertDTO.setType(AlertType.ALERTA_MEDIA);
		requestAlertDTO.setMessage("Warning alert");

		responseAlertDTO = new ResponseAlertDTO();
		responseAlertDTO.setId(1L);
		responseAlertDTO.setType(AlertType.ALERTA_ROJA);
		responseAlertDTO.setMessage("Alerta Roja");

		when(userService.getCurrentUserEmail()).thenReturn("user@example.com");
	}

	@Test
	void testGetAllAlerts() {
		when(alertRepository.findAllByUser_Email("user@example.com")).thenReturn(List.of(alert));
		when(modelMapperUtils.mapAll(any(), eq(ResponseAlertDTO.class))).thenReturn(List.of(responseAlertDTO));

		List<ResponseAlertDTO> alerts = alertService.getAllAlerts();

		assertEquals(1, alerts.size());
		assertEquals("Alerta Roja", alerts.get(0).getMessage());
	}

	@Test
	void testGetAlertById() {
		when(alertRepository.findByIdAndUser_Email(1L, "user@example.com")).thenReturn(Optional.of(alert));
		when(modelMapperUtils.map(alert, ResponseAlertDTO.class)).thenReturn(responseAlertDTO);

		ResponseAlertDTO result = alertService.getAlertById(1L);
		assertEquals("Alerta Roja", result.getMessage());
	}

	@Test
	void testCreateAlert() {
		when(readingRepository.findById(requestAlertDTO.getReadingId())).thenReturn(Optional.of(new ReadingEntity()));
		when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
		when(alertRepository.save(any())).thenReturn(alert);
		when(modelMapperUtils.map(requestAlertDTO, AlertEntity.class)).thenReturn(alert);
		when(modelMapperUtils.map(alert, ResponseAlertDTO.class)).thenReturn(responseAlertDTO);

		ResponseAlertDTO result = alertService.createAlert(requestAlertDTO);
		assertNotNull(result);
		assertEquals("Alerta Roja", result.getMessage());
	}

	@Test
	void testUpdateAlertById() {
		when(alertRepository.findByIdAndUser_Email(1L, "user@example.com")).thenReturn(Optional.of(alert));
		when(alertRepository.save(alert)).thenReturn(alert);
		when(modelMapperUtils.map(alert, ResponseAlertDTO.class)).thenReturn(responseAlertDTO);

		ResponseAlertDTO result = alertService.updateAlertById(1L, requestAlertDTO);
		assertNotNull(result);
		assertEquals("Alerta Roja", result.getMessage());
	}

	@Test
	void testDeleteAlertById() {
		when(alertRepository.findByIdAndUser_Email(1L, "user@example.com")).thenReturn(Optional.of(alert));
		doNothing().when(alertRepository).delete(alert);

		assertDoesNotThrow(() -> alertService.deleteAlertById(1L));
		verify(alertRepository, times(1)).delete(alert);
	}

	@Test
	void testGetAlertsByType() {
		when(alertRepository.findAllByTypeAndUser_Email(AlertType.ALERTA_ROJA, "user@example.com"))
				.thenReturn(List.of(alert));
		when(modelMapperUtils.mapAll(any(), eq(ResponseAlertDTO.class))).thenReturn(List.of(responseAlertDTO));

		List<ResponseAlertDTO> alerts = alertService.getAlertsByType(AlertType.ALERTA_ROJA);
		assertEquals(1, alerts.size());
		assertEquals("Alerta Roja", alerts.get(0).getMessage());
	}

	@Test
	void testGetAlertsByType_NotFound() {
		when(alertRepository.findAllByTypeAndUser_Email(AlertType.ALERTA_ROJA, "user@example.com"))
				.thenReturn(List.of(alert));
		when(modelMapperUtils.mapAll(any(), eq(ResponseAlertDTO.class))).thenReturn(List.of());

		List<ResponseAlertDTO> alerts = alertService.getAlertsByType(AlertType.ALERTA_ROJA);
		assertEquals(0, alerts.size());
	}

	@Test
	void testGetAlertsByReadingId_Success() {
		ReadingEntity reading = new ReadingEntity();
		when(alertRepository.findAllByReading_IdAndUser_Email(reading.getId(), "user@example.com"))
				.thenReturn(List.of(alert));
		when(modelMapperUtils.mapAll(any(), eq(ResponseAlertDTO.class)))
				.thenReturn(List.of(responseAlertDTO));

		List<ResponseAlertDTO> alerts = alertService.getAlertsByReadingId(reading);

		assertEquals(1, alerts.size());
		assertEquals("Alerta Roja", alerts.get(0).getMessage());
	}

	@Test
	void testGetAlertsByReadingId_ReadingEntityIsNull() {
		Exception exception = assertThrows(IllegalArgumentException.class, () -> {
			alertService.getAlertsByReadingId(null);
		});

		assertEquals("El objeto ReadingEntity no puede ser nulo.", exception.getMessage());
	}

	@Test
	void testGetAlertsByReadingId_UserEmailIsNull() {
		when(userService.getCurrentUserEmail()).thenReturn(null);

		Exception exception = assertThrows(IllegalStateException.class, () -> {
			alertService.getAlertsByReadingId(new ReadingEntity());
		});

		assertEquals("No se pudo obtener el email del usuario autenticado.", exception.getMessage());
	}

	@Test
	void testGetAlertsByReadingId_NoAlertsFound() {
		ReadingEntity reading = new ReadingEntity();
		when(alertRepository.findAllByReading_IdAndUser_Email(reading.getId(), "user@example.com")).thenReturn(List.of());

		Exception exception = assertThrows(EntityNotFoundException.class, () -> {
			alertService.getAlertsByReadingId(reading);
		});

		assertEquals("No hay alertas asociadas a la lectura para el usuario: user@example.com", exception.getMessage());
	}
}
