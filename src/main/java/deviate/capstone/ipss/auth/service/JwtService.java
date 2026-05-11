package deviate.capstone.ipss.auth.service;

import java.time.Instant;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import deviate.capstone.ipss.auth.entity.User;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    private final SecretKey key;

    public JwtService(@Value("${jwt.secret}") String base64Secret) {
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(base64Secret));
    }

    // Called after successful login — creates the token
    public String generateToken(User user) {
        Instant now = Instant.now();
        return Jwts.builder()
            .subject(user.getGovtEmail())
            .claim("role", user.getRole().name())
            .claim("userId", user.getId())
            .issuedAt(Date.from(now))
            .expiration(Date.from(now.plusSeconds(86400)))
            .signWith(key)
            .compact();
    }

    // Called on every protected request — checks if token is still valid
    public boolean isTokenValid(String token) {
        try {
            Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    // Extracts the email from inside the token
    public String extractEmail(String token) {
        return Jwts.parser()                //creates an object we can compare the incoming token with
            .verifyWith(key)                //Places the key to be used for comparison
            .build()                        //Ensures that the parser object can no longer be edited
            .parseSignedClaims(token)       //Compares the incoming token to the parser object we just created
            .getPayload()                   //If everything before went right, we get the payload content
            .getSubject();                  //Of the payload, we get only the email
    }
}
