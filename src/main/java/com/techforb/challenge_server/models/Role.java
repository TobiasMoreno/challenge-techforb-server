package com.techforb.challenge_server.models;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Set;

@RequiredArgsConstructor
public enum Role {
	USER(Set.of("READ")),
	ADMIN(Set.of("READ","WRITE", "DELETE"));

	@Getter
	private final Set<String> permissions;


}
