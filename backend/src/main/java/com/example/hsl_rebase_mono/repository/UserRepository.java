package com.example.hsl_rebase_mono.repository;

import com.example.hsl_rebase_mono.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByEmail(String email);

	Boolean existsByEmail(String email);
}
