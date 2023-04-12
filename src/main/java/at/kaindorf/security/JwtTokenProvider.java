package at.kaindorf.security;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Component
@Slf4j
public class JwtTokenProvider {

    @Value("${app.jwtSecret}")
    private String jwtSecret;

    public String generateToken(String userEmail) {
        //Get the current timestamp (Instant) and calculate the expiration time (7 days from now)
        Instant now = Instant.now();
        Instant expiration = now.plus(7, ChronoUnit.DAYS);

        try {
            //Build the JWT using Jwts.builder() with the provided user email as the subject, issued at time, expiration time,
            // and the signing algorithm (HS256) with the jwtSecret as the signing key
            return Jwts.builder()
                    .setSubject(userEmail)
                    .setIssuedAt(Date.from(now))
                    .setExpiration(Date.from(expiration))
                    .signWith(SignatureAlgorithm.HS256, jwtSecret.getBytes("UTF-8"))
                    .compact();

        } catch (UnsupportedEncodingException e) {
            //Handle any exception that may occur during JWT generation
            throw new RuntimeException(e);
        }

    }

    public String generateToken(Authentication authentication) {
        //Extract the user details (e.g., username) from the Authentication object
        User user = (User) authentication.getPrincipal();
        //Call the generateToken() method with the extracted user's username as the parameter
        return generateToken(user.getUsername());
    }

    public String getUserMailFromToken(String token) {
        Claims claims = null;
        try {
            //Parse the JWT token and retrieve the claims (e.g., subject, expiration, etc.)
            claims = Jwts
                    .parser()
                    .setSigningKey(jwtSecret.getBytes("UTF-8"))
                    .parseClaimsJws(token)
                    .getBody();
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
//Extract the subject (i.e., user email or username) from the claims
        return claims.getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(jwtSecret.getBytes("UTF-8")).parseClaimsJws(token);
            return true;
        } catch (SignatureException ex) {
            log.error("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            log.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            log.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty.");
        } catch (Exception ignored) {}

        return false;
    }

}
