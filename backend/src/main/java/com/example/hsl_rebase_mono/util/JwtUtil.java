package com.example.hsl_rebase_mono.util;

import com.example.hsl_rebase_mono.security.EnvironmentService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
@Slf4j
public class JwtUtil {

	private final EnvironmentService environmentService;

	public JwtUtil(EnvironmentService environmentService) {
		this.environmentService = environmentService;
	}

	public String generateToken(String username) {
		return Jwts.builder()
				.setSubject(username)
				.setExpiration(new Date(System.currentTimeMillis() + environmentService.getExpiration()))
				.signWith(Keys.hmacShaKeyFor(environmentService.getSecret().getBytes(StandardCharsets.UTF_8)))
				.compact();
	}

}
