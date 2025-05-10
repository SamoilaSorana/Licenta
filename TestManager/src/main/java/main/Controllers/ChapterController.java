package main.Controllers;


import Objects.Chapter;
import Objects.Lecture;
import main.DAO.ChapterDAO;
import main.DAO.LectureDAO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController

public class ChapterController {

    @GetMapping("/chapters/all")
    public ResponseEntity<?> findAll() {
        List<Chapter> chapters = ChapterDAO.findAll();
        return new ResponseEntity<>(chapters, HttpStatus.OK);
    }


}
