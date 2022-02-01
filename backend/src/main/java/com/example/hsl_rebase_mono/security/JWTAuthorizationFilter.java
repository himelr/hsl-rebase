package com.example.hsl_rebase_mono.security;

import com.google.common.base.Strings;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

	private final EnvironmentService environmentService;

	public JWTAuthorizationFilter(AuthenticationManager authManager, EnvironmentService environmentService) {
		super(authManager);
		this.environmentService = environmentService;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest req,
									HttpServletResponse res,
									FilterChain chain) throws IOException, ServletException {
		String authHeader = req.getHeader(environmentService.getHeader());
		if (authHeader == null || !authHeader.startsWith(environmentService.getTokenPrefix())) {
			chain.doFilter(req, res);
			return;
		}

		UsernamePasswordAuthenticationToken authentication = getAuthentication(req);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		chain.doFilter(req, res);
	}

	private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
		String token = request.getHeader(environmentService.getHeader());
		Jws<Claims> jws;
		try {
			jws = Jwts.parserBuilder()
					.setSigningKey(Keys.hmacShaKeyFor(environmentService.getSecret().getBytes(StandardCharsets.UTF_8)))
					.build()
					.parseClaimsJws(token.replace(environmentService.getTokenPrefix(), ""));

			String user = jws.getBody().getSubject();
			if (!Strings.isNullOrEmpty(user)) {
				return new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());
			}
		} catch (JwtException ex) {
			ex.printStackTrace();
			return null;
		}
		return null;
	}
}
