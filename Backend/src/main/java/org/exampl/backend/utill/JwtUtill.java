package org.exampl.backend.utill;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtill {
    @Value("${jwt.accessExpiration}")
    private long accessExpiration;

    @Value("${jwt.refeshExpiration}")
    private long refeshExpiration;

    @Value("${jwt.secreat}")
    private String secretKey;

    public String generateAccessToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .claim("tokenType", "access")   // 🔑 mark as access token
                .setIssuedAt(new Date())
                .setExpiration(new Date
                        (System.currentTimeMillis() + accessExpiration))
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes())
                        , SignatureAlgorithm.HS256).compact();
    }

    public String generateRefreshToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .claim("tokenType", "refresh")  // 🔑 mark as refresh token
                .setIssuedAt(new Date())
                .setExpiration(new Date
                        (System.currentTimeMillis() + refeshExpiration))
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes())
                        , SignatureAlgorithm.HS256).compact();
    }
    public String extractUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
    public boolean validateToken(String token) {
        try {

            Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor
                            (secretKey.getBytes()))
                    .build()
                    .parseClaimsJws(token);
            System.out.println("validateToken "+true);
            return true;
        } catch (Exception e) {
            System.out.println("validateToken "+e.getMessage());
            return false;
        }
    }
    public String getTokenType(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("tokenType", String.class);
    }
}

