package main.Controllers;

import Objects.Answer;
import main.DAO.AnswerDAO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
public class AnswerController {

    @PostMapping("/answer")
    public ResponseEntity<?> add(@RequestBody Answer answer) {
        boolean inserted = AnswerDAO.insert(answer);
        if (inserted) {
            return new ResponseEntity<>(answer, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(null, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @GetMapping("/answer")
    public ResponseEntity<List<Answer>> getAll() {
        List<Answer> answers = AnswerDAO.findAll();
        return new ResponseEntity<>(answers, HttpStatus.OK);
    }

    @GetMapping("/answer/{id}")
    public ResponseEntity<?> getById(@PathVariable int id) {
        Answer answer = AnswerDAO.findById(id);
        if (answer == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(answer, HttpStatus.OK);
    }
}
