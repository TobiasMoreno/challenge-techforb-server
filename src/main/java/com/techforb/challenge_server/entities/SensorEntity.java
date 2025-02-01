package com.techforb.challenge_server.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Entity
@Table(name = "sensors")
@AllArgsConstructor
@NoArgsConstructor
public class SensorEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String type;

	@Column(nullable = false)
	private boolean isAvailable;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "plant_id", nullable = false)
	private PlantEntity plant;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "user_id", nullable = false)
	private UserEntity user;
}
