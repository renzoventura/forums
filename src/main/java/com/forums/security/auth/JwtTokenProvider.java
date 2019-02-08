package com.forums.security.auth;

import com.forums.security.CustomUserDetailsService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {

  private String secretKey = "secret"; // to encode

  private long validityInMilliseconds = 600000; // 2 mins

  @Autowired
  private CustomUserDetailsService userDetailsService;

  @PostConstruct
  protected void init() {
    secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
  }

  public String createToken(String username, List<String> roles) {
    Claims claims = Jwts.claims().setSubject(username);
    claims.put("roles", roles);
    Date now = new Date();
    Date validity = new Date(now.getTime() + validityInMilliseconds);

    return Jwts.builder()  //constructor has private access
            .setClaims(claims)
            .setIssuedAt(now)
            .setExpiration(validity)
            .signWith(SignatureAlgorithm.HS256, secretKey)
            .compact();
  }

  public Authentication getAuthentication(String token){
    UserDetails userDetails = this.userDetailsService.loadUserByUsername(getUserName(token));
    return new UsernamePasswordAuthenticationToken(userDetails,"", userDetails.getAuthorities());
  }

  public String getUserName(String token){
    return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
  }

  public String getTokenFromRequest(HttpServletRequest request){
    String token = request.getHeader("Authorization");
    if (token != null && token.startsWith("Bearer ")){
      return token.substring(7,token.length());
    }
    return null;
  }

  public boolean validateToken(String token){
    try {
      //JWS - JSON web start, JWT  uses JWS for its signature, from the spec:
      Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
      if (claims.getBody().getExpiration().before(new Date())){
        return false;
      }
      return true;
    } catch (JwtException e){
      throw new JwtException("Jwt Token Expired;");
    }
  }

}
