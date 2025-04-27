package main.Controllers;

import Objects.QuestionForLecture;
import main.DAO.QuestionForLectureDAO;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/question_for_lecture")
public class QuestionForLectureController {

    @PostMapping
    public ResponseEntity<?> add(@RequestBody QuestionForLecture qfl) {
        boolean inserted = QuestionForLectureDAO.insert(qfl);
        return inserted ?
                ResponseEntity.ok("Inserted!") :
                ResponseEntity.unprocessableEntity().body("Insert failed!");
    }

    @GetMapping
    public ResponseEntity<List<QuestionForLecture>> getAll() {
        return ResponseEntity.ok(QuestionForLectureDAO.findAll());
    }
}
