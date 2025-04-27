package main.Controllers;

import Objects.Lecture;
import main.DAO.LectureDAO;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
public class LectureController {

    @GetMapping("/lecture/all")
    public ResponseEntity<?> findAll() {
        List<Lecture> list = LectureDAO.findAll();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/lecture/{id}")
    public ResponseEntity<?> findById(@PathVariable("id") Integer id) {
        Lecture lecture = LectureDAO.findById(id);
        if (lecture == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(lecture, HttpStatus.OK);
    }

    @PostMapping("/lecture")
    public ResponseEntity<?> add(@RequestBody Lecture lecture) {
        boolean inserted = LectureDAO.insert(lecture);
        if (inserted) {
            lecture = LectureDAO.findLast(); // sau după ID dacă îl știi
            return new ResponseEntity<>(lecture, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(null, HttpStatus.UNPROCESSABLE_ENTITY);
    }
}
