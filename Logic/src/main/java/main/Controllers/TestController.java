package main.Controllers;
import Objects.AnswerFromClient;
import Objects.Chapter;
import Objects.Completed;
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
public class TestController {
    @PostMapping("/lecture/{id}/test/submit")
    public ResponseEntity<?> SubmitTest(@RequestBody List<AnswerFromClient>answers,@RequestHeader("Authorization") String authHeader, @PathVariable int id) {
        int userId = getIdfromheader(authHeader);
        TestLogic.submitAttemptFromAnswers(userId,id,answers);
        double grade=TestLogic.EvaluateTest(id,answers);
        if(grade>6)
        {
            Completed completed=new Completed(userId,id);
            TestLogic.sendCompleted(completed);
        }
        return new ResponseEntity<>(grade, HttpStatus.OK);
    }

    @GetMapping("/attempts/all")
    public ResponseEntity<?> getAttempts(@RequestHeader("Authorization") String authHeader) {
        int id = getIdfromheader(authHeader);
        List<Map<String,Object>> attempts = TestLogic.getAllEvaluatedAttempts(id);
        return new ResponseEntity<>(attempts, HttpStatus.OK);
    }




}
