package com.example.hsl_rebase_mono.security;

import com.example.hsl_rebase_mono.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private final AuthenticationManager authenticationManager;
	private final EnvironmentService environmentService;
	private final JwtUtil jwtUtil;

	public JWTAuthenticationFilter(AuthenticationManager authenticationManager, EnvironmentService environmentService, JwtUtil jwtUtil) {
		this.authenticationManager = authenticationManager;
		this.environmentService = environmentService;
		this.jwtUtil = jwtUtil;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res) throws AuthenticationException {
		try {
			User user = new ObjectMapper().readValue(req.getInputStream(), User.class);
			return authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(
							user.getUsername(),
							user.getPassword(),
							user.getAuthorities()
			));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest req,
											HttpServletResponse res,
											FilterChain chain,
											Authentication auth) {
		String token = jwtUtil.generateToken(((User) auth.getPrincipal()).getUsername());
		res.addHeader(environmentService.getHeader(), environmentService.getTokenPrefix() + token);
	}

}
