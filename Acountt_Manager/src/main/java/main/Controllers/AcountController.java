package main.Controllers;
import main.DAO.AcountDAO;
import main.DAO.RolDAO;
import main.Objects.Acount;
import main.Objects.Rol;
import main.Sistem.JwtAuthenticationResponse;
import main.Sistem.JwtTokenUtil;
import main.Sistem.LoginRequest;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.List;

import static javax.swing.text.html.parser.DTDConstants.ID;

@RestController
@CrossOrigin(origins = "*")
public class AcountController {
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @GetMapping("/admin/acounts")
    public ResponseEntity<?> findAll() {
        List<Acount> list = AcountDAO.findAll();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @PostMapping("/user/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        // Fetch the user by username from your database
        Acount user = AcountDAO.findByUsername(loginRequest.getUsername());

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found");
        }

        // Verify the password; assume verifyPassword handles hashing/encoding
        if (!authenticate(loginRequest.getUsername(),loginRequest.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }

        // Generate a token using your JWT utility; you might need to adjust the token generation
        String token = jwtTokenUtil.generateTokenFromUser(user);
        return ResponseEntity.ok(new JwtAuthenticationResponse(token));
    }

    @GetMapping("/admin/acounts/{id}")
    public ResponseEntity<?> findByID(@PathVariable("id") Integer id) {
        Acount acount = AcountDAO.findById_User(id);
        if (acount == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(acount, HttpStatus.OK); // Returnăm contul găsit
    }



    public boolean authenticate(String username, String rawPassword) {
        System.out.println(username);
        Acount user = AcountDAO.findByUsername(username);
        System.out.println(user);
        if (user != null) {
            return passwordEncoder.matches(rawPassword, user.getParola());
        }
        return false;
    }

    @PostMapping("/user/register")
    public ResponseEntity<?> add(@RequestBody Acount acount) {
        System.out.println("🚀 Primit cont nou: " + acount);

        // Conversie din String în obiect Rol
        Rol rol = RolDAO.findByNume(acount.getRolInput());
        if (rol == null) {
            return ResponseEntity.badRequest().body("Rol invalid!");
        }

        acount.setRol(rol);
        acount.setParola(passwordEncoder.encode(acount.getParola()));

        boolean inserted = AcountDAO.insert(acount);

        if (inserted) {
            acount = AcountDAO.findById_User(acount.getID());
            return new ResponseEntity<>(acount, HttpStatus.CREATED);
        }

        return new ResponseEntity<>(null, HttpStatus.UNPROCESSABLE_ENTITY);
    }

}