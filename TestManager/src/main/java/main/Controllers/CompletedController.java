package main.Controllers;

import Objects.Answer;
import Objects.Completed;
import main.DAO.AnswerDAO;
import main.DAO.CompletedDAO;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CompletedController {

    @PostMapping("/completed")
    public ResponseEntity<?> add(@RequestBody Completed completed) {
        boolean inserted = CompletedDAO.insert(completed);
        if (inserted) {
            return new ResponseEntity<>(completed, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(null, HttpStatus.UNPROCESSABLE_ENTITY);
    }
    @GetMapping("/completed/user/{id}")
    public ResponseEntity<?> getById(@PathVariable int id) {
        List<Completed> completedList = CompletedDAO.findById(id);
        return new ResponseEntity<>(completedList, HttpStatus.OK);

    }
}
