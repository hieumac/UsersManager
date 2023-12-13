package com.example.demo.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.demo.Entity.User.User;

import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

  private static final String secretKey = "5A7134743777217A25432A46294A404E635266556A586E3272357538782F413F" ;

  public String generateToken(User user, Collection<SimpleGrantedAuthority> authorities) {
      Algorithm algorithm = Algorithm.HMAC256(secretKey.getBytes());
    return JWT
            .create()
            .withSubject(user.getEmail())
            .withExpiresAt(new Date(System.currentTimeMillis()+50*60*1000))
            .withClaim("roles", authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
            .sign(algorithm);
  }

  public String getSecretKey() {
    return secretKey;
  }
}
