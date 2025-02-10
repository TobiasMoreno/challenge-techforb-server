package com.techforb.challenge_server.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "readings")
@AllArgsConstructor
@NoArgsConstructor
public class ReadingEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name= "reading_value",nullable = false)
	private double readingValue;

	@Column(nullable = false)
	private LocalDateTime timestamp;

	@ManyToOne
	@JoinColumn(name = "sensor_id", nullable = false)
	private SensorEntity sensor;

	@OneToMany(mappedBy = "reading", cascade = CascadeType.ALL)
	private List<AlertEntity> alerts;

	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private UserEntity user;
}
