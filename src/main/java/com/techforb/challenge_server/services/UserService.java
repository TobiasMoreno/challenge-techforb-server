package com.techforb.challenge_server.services;

import com.techforb.challenge_server.dtos.user.ResponseUserDTO;
import com.techforb.challenge_server.entities.UserEntity;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
	ResponseUserDTO getUserByEmail(String email);

	String getCurrentUserEmail();

	UserEntity getCurrentUserEntity();
}
