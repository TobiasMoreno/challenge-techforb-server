package com.techforb.challenge_server.entities;

import com.techforb.challenge_server.models.AlertType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "alerts")
@AllArgsConstructor
@NoArgsConstructor
public class AlertEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private AlertType type;

	@Column(nullable = false)
	private String message;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "reading_id", nullable = false)
	private ReadingEntity reading;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "sensor_id", nullable = false)
	private SensorEntity sensor;

	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private UserEntity user;
}
