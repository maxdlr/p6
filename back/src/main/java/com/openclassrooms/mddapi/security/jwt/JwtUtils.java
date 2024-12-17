package com.openclassrooms.mddapi.security.jwt;

import com.openclassrooms.mddapi.security.services.UserDetailsImpl;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class JwtUtils {
  private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

  //  private final Key jwtSecret = Jwts.SIG.HS512.key().build();
  @Value("${oc.app.jwtSecret}")
  private String jwtSecret;

  @Value("${oc.app.jwtExpirationMs}")
  private int jwtExpirationMs;

  //  private byte[] getSigningKey() {
  //    return Jwts.SIG.HS512.key().build().getEncoded();
  //  }

  public String generateJwtToken(Authentication authentication) {

    UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

    return Jwts.builder()
        .subject((userPrincipal.getUsername()))
        .issuedAt(new Date())
        .expiration(new Date((new Date()).getTime() + jwtExpirationMs))
        .signWith(Keys.hmacShaKeyFor(jwtSecret.getBytes()), Jwts.SIG.HS512)
        .compact();
  }

  public String getUserNameFromJwtToken(String token) {
    String username =
        Jwts.parser()
            .verifyWith(Keys.hmacShaKeyFor(jwtSecret.getBytes()))
            .build()
            .parseSignedClaims(token)
            .getPayload()
            .getSubject();

    System.out.println(username);

    return username;
  }

  public boolean validateJwtToken(String authToken) {
    try {
      byte[] secretKeyBytes = jwtSecret.getBytes();
      Jwts.parser()
          .verifyWith(Keys.hmacShaKeyFor(secretKeyBytes))
          .build()
          .parseSignedClaims(authToken);
      return true;
    } catch (SignatureException e) {
      logger.error("Invalid JWT signature: {}", e.getMessage());
    } catch (MalformedJwtException e) {
      logger.error("Invalid JWT token: {}", e.getMessage());
      logger.error(Keys.hmacShaKeyFor(jwtSecret.getBytes()).toString());
    } catch (ExpiredJwtException e) {
      logger.error("JWT token is expired: {}", e.getMessage());
    } catch (UnsupportedJwtException e) {
      logger.error("JWT token is unsupported: {}", e.getMessage());
    } catch (IllegalArgumentException e) {
      logger.error("JWT claims string is empty: {}", e.getMessage());
    }

    return false;
  }
}
