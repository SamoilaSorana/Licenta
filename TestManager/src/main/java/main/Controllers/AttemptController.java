package main.Controllers;

import Objects.Attempt;
import main.DAO.AttemptDAO;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
public class AttemptController {

    @PostMapping("/attempt")
    public ResponseEntity<?> add(@RequestBody Attempt attempt) {
        int inserted = AttemptDAO.insert(attempt);
        return inserted!=-1
                ? new ResponseEntity<>(inserted, HttpStatus.CREATED)
                : new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @GetMapping("/attempt")
    public ResponseEntity<List<Attempt>> getAll() {
        return new ResponseEntity<>(AttemptDAO.findAll(), HttpStatus.OK);
    }
}
