package com.techforb.challenge_server.entities;

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
	private String type;

	@Column(nullable = false)
	private String message;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "reading_id", nullable = false)
	private ReadingEntity reading;

	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private UserEntity user;
}
