package com.techforb.challenge_server.services;

import com.techforb.challenge_server.models.User;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
	User getUserByEmail(String email);

	String getCurrentUserEmail();
}
