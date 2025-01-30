package com.techforb.challenge_server.models;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
public enum Role {
	USER(Set.of("READ")),
	ADMIN(Set.of("READ","WRITE", "DELETE"));

	private final Set<String> permissions;

	public List<SimpleGrantedAuthority> getAuthorities() {
		var authorities = permissions
				.stream()
				.map(SimpleGrantedAuthority::new)
				.collect(Collectors.toList());
		authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
		return authorities;
	}

}
