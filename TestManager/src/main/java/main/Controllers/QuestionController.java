package main.Controllers;

import main.DAO.QuestionDAO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
public class QuestionController {

    @GetMapping("/question")
    public ResponseEntity<?> findAll() {
        List<Question> list = QuestionDAO.findAll();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @PostMapping("/question")
    public ResponseEntity<?> add(@RequestBody Question question) {
        boolean inserted = QuestionDAO.insert(question);
        if (inserted) {
            question = QuestionDAO.findLast();
            return new ResponseEntity<>(question, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(null, HttpStatus.UNPROCESSABLE_ENTITY);
    }
}
