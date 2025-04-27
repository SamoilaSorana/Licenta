package main.Controllers;

import Objects.RequiredLecture;
import main.DAO.RequiredLectureDAO;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/required_lecture")
public class RequiredLectureController {

    @PostMapping
    public ResponseEntity<?> add(@RequestBody RequiredLecture requiredLecture) {
        boolean inserted = RequiredLectureDAO.insert(requiredLecture);
        if (inserted) {
            return new ResponseEntity<>(requiredLecture, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(null, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @GetMapping
    public ResponseEntity<List<RequiredLecture>> getAll() {
        return new ResponseEntity<>(RequiredLectureDAO.findAll(), HttpStatus.OK);
    }

    @DeleteMapping("/{currentLectureId}/{requiredLectureId}")
    public ResponseEntity<?> delete(@PathVariable int currentLectureId, @PathVariable int requiredLectureId) {
        boolean deleted = RequiredLectureDAO.delete(currentLectureId, requiredLectureId);
        if (deleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/{currentLectureId}/{requiredLectureId}")
    public ResponseEntity<?> update(@PathVariable int currentLectureId,
                                    @PathVariable int requiredLectureId,
                                    @RequestBody RequiredLecture newValues) {
        boolean updated = RequiredLectureDAO.update(currentLectureId, requiredLectureId, newValues);
        if (updated) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
