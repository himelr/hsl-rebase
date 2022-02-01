package com.example.hsl_rebase_mono.repository;

import org.springframework.stereotype.Repository;

@Repository
public class BaseRepository {

	private final UserRepository userRepository;

	public BaseRepository(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public UserRepository userRepository() {
		return userRepository;
	}
}
