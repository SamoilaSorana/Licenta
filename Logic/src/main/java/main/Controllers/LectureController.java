package main.Controllers;


import Objects.Chapter;
import Objects.Lecture;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import main.Logic.LecturesLogic;
import main.Logic.TestLogic;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Base64;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;

import java.util.List;

import static main.Sistem.HelperFunctions.getIdfromheader;

@RestController
@CrossOrigin(origins = "*")
public class LectureController {



    @GetMapping("/lecture/all")
    public ResponseEntity<?> getall(@RequestHeader("Authorization") String authHeader) {
        int id = getIdfromheader(authHeader);
        List<Map<String,Object>> lectureList = LecturesLogic.GetLectures(id);
        return new ResponseEntity<>(lectureList, HttpStatus.OK);
    }

    @GetMapping("/lecture/{id}/test")
    public ResponseEntity<?> gettest(@RequestHeader("Authorization") String authHeader, @PathVariable int id) {
      return new ResponseEntity<>(TestLogic.getQuestionForTest(id), HttpStatus.OK);
    }


    @GetMapping("/lecture/chapter/{chapterId}")
    public ResponseEntity<?> getLecturesByChapter(@PathVariable int chapterId) {
        List<Lecture> lectures = LecturesLogic.getLecturesByChapterId(chapterId);
        return new ResponseEntity<>(lectures, HttpStatus.OK);}


    @GetMapping("/lecture/all_unfiltered")
    public ResponseEntity<?> getall_unfiltered(@RequestHeader("Authorization") String authHeader) {
        int id = getIdfromheader(authHeader);
        List<Map<String,Object>> lectureList = LecturesLogic.GetLecturesWithoutFilter(id);
        return new ResponseEntity<>(lectureList, HttpStatus.OK);
    }
}
