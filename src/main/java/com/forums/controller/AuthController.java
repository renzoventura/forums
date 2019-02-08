package com.forums.controller;

import static java.util.stream.Collectors.toList;
import static org.springframework.http.ResponseEntity.ok;

import com.forums.model.Account;
import com.forums.model.AuthenticationRequest;
import com.forums.repository.AccountRepository;
import com.forums.security.auth.JwtTokenProvider;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

  @Autowired
  AccountRepository accountRepository;

  @Autowired
  JwtTokenProvider jwtTokenProvider;

  @Autowired
  AuthenticationManager authenticationManager;

  @Autowired
  PasswordEncoder encoder;

  @PostMapping("/signin")
  public ResponseEntity signin(@RequestBody AuthenticationRequest data){
    try {
      String username = data.getUsername();
      String password = data.getPassword();
      authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username,password));

      String token = jwtTokenProvider.createToken(username, this.accountRepository
          .findByUsername(username).orElseThrow(()
              -> new UsernameNotFoundException("Username " + username + "not found")).getRoles());

      Map<Object,Object> model = new HashMap<>();
      model.put("username", username);
      model.put("token", token);
      return ok(model);

    } catch (Exception e ){
      throw new BadCredentialsException("Invalid Account");
    }
  }

  @PostMapping("/user/signup")
  public ResponseEntity signupUser(@RequestBody Account account) {
    if (!accountRepository.findByUsername(account.getUsername()).isPresent()) {
      account.setPassword(encoder.encode(account.getPassword()));
      account.setRoles(Arrays.asList("ROLE_USER"));
      return ok(accountRepository.save(account));
    }
    throw new IllegalArgumentException("The username is already in use");
  }

  @PostMapping("/admin/signup")
  public ResponseEntity signupAdmin(@RequestBody Account account) {
    if (!accountRepository.findByUsername(account.getUsername()).isPresent()) {
      account.setPassword(encoder.encode(account.getPassword()));
      account.setRoles(Arrays.asList("ROLE_USER", "ROLE_ADMIN"));
      return ok(accountRepository.save(account));
    }
    throw new IllegalArgumentException("The username is already in use");
  }


  @GetMapping("/me")
  public ResponseEntity getCurrentUserDetails(@AuthenticationPrincipal UserDetails userDetails){
    Map<Object,Object> account = new HashMap<>();
    account.put("username", userDetails.getUsername());
    account.put("roles", userDetails.getAuthorities().stream()
        .map(a -> ((GrantedAuthority) a).getAuthority())
        .collect(toList()));
    return ok(account);
  }

}
