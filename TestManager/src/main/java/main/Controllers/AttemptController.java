package main.Controllers;

import Objects.*;
import main.DAO.AnswerDAO;
import main.DAO.AttemptDAO;

import main.DAO.AttemptInfoDAO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
public class AttemptController {

    @PostMapping("/attempt")
    public ResponseEntity<?> add(@RequestBody Attempt attempt) {
        int inserted = AttemptDAO.insert(attempt);
        return inserted!=-1
                ? new ResponseEntity<>(inserted, HttpStatus.CREATED)
                : new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @GetMapping("/attempt")
    public ResponseEntity<List<Attempt>> getAll() {
        return new ResponseEntity<>(AttemptDAO.findAll(), HttpStatus.OK);

    }
    @GetMapping("/attempt/count/{user_id}/{lecture_id}")
    public ResponseEntity<?> getById(@PathVariable int user_id,@PathVariable int lecture_id) {
        int count = AttemptDAO.findcount(user_id,lecture_id);

        return new ResponseEntity<>(count, HttpStatus.OK);
    }

    @GetMapping("/attempt/{attemptId}/answers")
    public ResponseEntity<?> getAnswersFromAttempt(@PathVariable int attemptId) {
        List<AttemptInfo> infos = AttemptInfoDAO.findByAttemptId(attemptId);

        if (infos == null || infos.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Map<Integer, List<Integer>> grouped = new HashMap<>();
        for (AttemptInfo info : infos) {
            grouped.computeIfAbsent(info.getQuestionId(), k -> new ArrayList<>())
                    .add(info.getAnswerId());
        }

        List<AnswerFromClient> result = new ArrayList<>();
        for (Map.Entry<Integer, List<Integer>> entry : grouped.entrySet()) {
            AnswerFromClient afc = new AnswerFromClient();
            afc.setQuestionId(entry.getKey());
            afc.setAnswerIds(entry.getValue());
            result.add(afc);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    @GetMapping("/user/{id}/attempts")
    public ResponseEntity<?> getAllAttemptAnswers(@PathVariable("id") int id) {
        List<Attempt> allAttempts = AttemptDAO.findAllbyUserId(id);
        List<AttemptAnswers> result = new ArrayList<>();

        for (Attempt attempt : allAttempts) {
            List<AttemptInfo> infos = AttemptInfoDAO.findByAttemptId(attempt.getAttemptId());


            Map<Integer, List<Integer>> grouped = new HashMap<>();
            for (AttemptInfo info : infos) {
                grouped.computeIfAbsent(info.getQuestionId(), k -> new ArrayList<>())
                        .add(info.getAnswerId());
            }


            List<AnswerFromClient> answers = new ArrayList<>();
            for (Map.Entry<Integer, List<Integer>> entry : grouped.entrySet()) {
                answers.add(new AnswerFromClient(entry.getKey(), entry.getValue()));
            }

            AttemptAnswers wrapped = new AttemptAnswers();

            wrapped.setAttemptId(attempt.getAttemptId());
            wrapped.setLectureId(attempt.getLectureId());
            wrapped.setAnswers(answers);
            result.add(wrapped);
        }

        return ResponseEntity.ok(result);
    }


}
