package main.Controllers;

import main.Sistem.JwtTokenUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final JwtTokenUtil jwtTokenUtil;

    public AuthController(JwtTokenUtil jwtTokenUtil) {
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @GetMapping("/check-token")
    public ResponseEntity<?> checkToken(@RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.startsWith("Bearer ") ? authHeader.substring(7) : authHeader;
            boolean isValid = jwtTokenUtil.validateToken(token);
            if (isValid) {
                String role = jwtTokenUtil.getRoleFromToken(token);
                String username = jwtTokenUtil.getUsernameFromToken(token);
                List<String> permisiuni = jwtTokenUtil.getPermissionsFromToken(token);

                return ResponseEntity.ok(Map.of(
                        "username", username,
                        "role", role,
                        "permisiuni", permisiuni
                ));
            } else {
                return ResponseEntity.status(401).body("Invalid token");
            }
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Invalid token");
        }
    }
}