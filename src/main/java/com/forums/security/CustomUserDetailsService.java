package com.forums.security;

import com.forums.repository.AccountRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class CustomUserDetailsService implements UserDetailsService {

  AccountRepository accounts;

  public CustomUserDetailsService(AccountRepository accounts) {
    this.accounts = accounts;
  }

  @Override //since Account implements UserDetails
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return this.accounts.findByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException("Username: " + username + " not found"));
  }
}
