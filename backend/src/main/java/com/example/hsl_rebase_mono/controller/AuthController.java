package com.example.hsl_rebase_mono.controller;

import com.example.hsl_rebase_mono.dto.user.AuthDto;
import com.example.hsl_rebase_mono.dto.user.UserDto;
import com.example.hsl_rebase_mono.exception.ForbiddenException;
import com.example.hsl_rebase_mono.exception.ServiceException;
import com.example.hsl_rebase_mono.models.User;
import com.example.hsl_rebase_mono.repository.BaseRepository;
import com.example.hsl_rebase_mono.util.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	private final BaseRepository baseRepository;
	private final JwtUtil jwtUtil;
	private final PasswordEncoder passwordEncoder;

	public AuthController(BaseRepository baseRepository, JwtUtil jwtUtil, PasswordEncoder passwordEncoder) {
		this.baseRepository = baseRepository;
		this.jwtUtil = jwtUtil;
		this.passwordEncoder = passwordEncoder;
	}

	@PostMapping("/login")
	public AuthDto login(@RequestBody UserDto userDto) {
		Optional<User> user = baseRepository.userRepository().findByEmail(userDto.getEmail());
		if (user.isEmpty() || !passwordEncoder.matches(userDto.getPassword(), user.get().getPassword())) {
			throw new ForbiddenException("Authentication failed");
		}
		AuthDto authDto = new AuthDto();
		authDto.setAccessToken(jwtUtil.generateToken(userDto.getEmail()));
		return authDto;
	}

	@PostMapping("/sign-up")
	public void signUp(@RequestBody UserDto userDto) {
 		if (baseRepository.userRepository().existsByEmail(userDto.getEmail())) {
			 throw new ServiceException("User exists");
		}
		User user = new User();
		user.setPassword(passwordEncoder.encode(userDto.getPassword()));
		user.setEmail(userDto.getEmail());

		baseRepository.userRepository().save(user);
	}
}
