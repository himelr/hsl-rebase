package com.example.hsl_rebase_mono.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service
public class EnvironmentService {

	@Value("${security.secret}")
	private String secret;

	@Value("${security.token-prefix}")
	private String tokenPrefix;

	@Value("${security.header}")
	private String header;

	@Value("${security.expiration}")
	private int expiration;

	private final Environment env;

	public EnvironmentService(Environment env) {
		this.env = env;
	}

	public String getSecret() {
		return secret;
	}

	public String getTokenPrefix() {
		return tokenPrefix;
	}

	public String getHeader() {
		return header;
	}

	public int getExpiration() {
		return expiration;
	}

	public Environment getEnv() {
		return env;
	}

}
