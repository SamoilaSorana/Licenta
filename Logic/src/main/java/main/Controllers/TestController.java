package main.Controllers;
import Objects.*;
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
import java.time.LocalDateTime;
import java.sql.Timestamp;

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
        if(grade>=6)
        {
            Completed completed=new Completed(userId,id);
            TestLogic.sendCompleted(completed);
        }
        else {
            int count= TestLogic.Getcount(userId,id);
            if(count>=3)
            {
                Help help=new Help();
                help.setUserId(userId);
                help.setDate(LocalDateTime.now());
                help.setRead(0);
                TestLogic.sendHelp(help);
            }
        }
        return new ResponseEntity<>(grade, HttpStatus.OK);
    }

    @GetMapping("/attempts/all")
    public ResponseEntity<?> getAttempts(@RequestHeader("Authorization") String authHeader) {
        int id = getIdfromheader(authHeader);
        List<Map<String,Object>> attempts = TestLogic.getAllEvaluatedAttempts(id);
        return new ResponseEntity<>(attempts, HttpStatus.OK);
    }

    @GetMapping("/attempts/all/{id}")
    public ResponseEntity<?> getAttempts(@RequestHeader("Authorization") String authHeader,@PathVariable int id) {
        List<Map<String,Object>> attempts = TestLogic.getAllEvaluatedAttempts(id);
        return new ResponseEntity<>(attempts, HttpStatus.OK);
    }





}
