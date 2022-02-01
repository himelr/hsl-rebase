package com.example.hsl_rebase_mono.security;

import com.example.hsl_rebase_mono.models.User;
import com.example.hsl_rebase_mono.repository.BaseRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements org.springframework.security.core.userdetails.UserDetailsService {

	private final BaseRepository baseRepository;

	public UserDetailsServiceImpl(BaseRepository baseRepository) {
		this.baseRepository = baseRepository;
	}

	@Transactional
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Optional<User> user = baseRepository.userRepository().findByEmail(email);
		if (user.isEmpty()) {
			throw new UsernameNotFoundException(email);
		}
	 	return new org.springframework.security.core.userdetails.User(
				 user.get().getEmail(),
				 user.get().getPassword(),
				 user.get().getRoles());
	}
}
