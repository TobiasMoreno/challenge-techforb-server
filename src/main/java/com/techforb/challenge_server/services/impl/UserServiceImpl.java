package com.techforb.challenge_server.services.impl;

import com.techforb.challenge_server.repositories.UserRepository;
import com.techforb.challenge_server.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;

}
