package main.Controllers;

import Objects.QuestionAnswer;
import main.DAO.QuestionAnswerDAO;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
public class QuestionAnswerController {

    @PostMapping("/question_answer")
    public ResponseEntity<?> add(@RequestBody QuestionAnswer qa) {
        boolean inserted = QuestionAnswerDAO.insert(qa);
        if (inserted) {
            return new ResponseEntity<>(qa, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(null, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @GetMapping("/question_answer")
    public ResponseEntity<List<QuestionAnswer>> getAll() {
        return new ResponseEntity<>(QuestionAnswerDAO.findAll(), HttpStatus.OK);
    }

    @GetMapping("/question_answer/question/{questionId}")
    public ResponseEntity<List<QuestionAnswer>> getByQuestionId(@PathVariable int questionId) {
        return new ResponseEntity<>(QuestionAnswerDAO.findByQuestionId(questionId), HttpStatus.OK);
    }
}
