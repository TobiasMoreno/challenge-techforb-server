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

	@Column(nullable = false, name = "is_available")
	private boolean isAvailable;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "plant_id", nullable = false)
	private PlantEntity plant;

	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private UserEntity user;

	@OneToMany(mappedBy = "sensor", cascade = CascadeType.ALL)
	private List<ReadingEntity> readings;
}
