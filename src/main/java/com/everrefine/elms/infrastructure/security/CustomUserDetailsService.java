package com.everrefine.elms.infrastructure.security;

import com.everrefine.elms.domain.model.user.EmailAddress;
import com.everrefine.elms.domain.model.user.User;
import com.everrefine.elms.domain.repository.UserRepository;
import java.util.List;
import java.util.UUID;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

  private final UserRepository userRepository;

  public CustomUserDetailsService(UserRepository repo) {
    this.userRepository = repo;
  }

  @Override
  public UserDetails loadUserByUsername(String emailAddress) {
    User user = userRepository.findUserByEmailAddress(new EmailAddress(emailAddress))
        .orElseThrow(() -> new UsernameNotFoundException("User not found"));

    return new org.springframework.security.core.userdetails.User(
        user.getId().toString(),
        user.getPassword().getValue(),
        List.of(new SimpleGrantedAuthority("ROLE_USER"))
    );
  }

  public UserDetails loadUserById(String userId) {
    User user = userRepository.findUserById(UUID.fromString(userId))
        .orElseThrow(() -> new UsernameNotFoundException("User not found"));

    return new org.springframework.security.core.userdetails.User(
        user.getId().toString(),
        user.getPassword().getValue(),
        List.of(new SimpleGrantedAuthority("ROLE_USER"))
    );
  }
}
