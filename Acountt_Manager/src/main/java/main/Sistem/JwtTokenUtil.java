package main.Sistem;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import main.Objects.Acount;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class JwtTokenUtil {

    private final String secret = "mySecretKey";
    private final long expiration = 604800L; // 7 days in seconds


    // New method that generates a token based on your User object
    public String generateTokenFromUser(Acount user) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration * 1000);
        System.out.println(user.getRol().getPermisiuniString());
        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim("role", user.getRol().getRol_name())
                .claim("permisiuni", user.getRol().getPermisiuniString())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    public String getUsernameFromToken(String token) {
        Claims claims = extractClaims(token);
        return claims.getSubject(); // Username is stored as "sub"
    }
    public List<String> getPermissionsFromToken(String token) {
        Claims claims = extractClaims(token);
        return claims.get("permisiuni", List.class); // It will return a List<String>
    }
    public String getRoleFromToken(String token) {
        Claims claims = extractClaims(token);
        return claims.get("role", String.class); // Extract the "role" claim
    }
    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secret).parseClaimsJws(token); // Parse and validate
            return true;
        } catch (Exception e) {
            System.out.println(":x: Invalid JWT: " + e.getMessage()); // Debugging
            return false;
        }
    }
    private Claims extractClaims(String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }
}
