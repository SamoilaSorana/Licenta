package main.Logic;

import Objects.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TestLogic {
    public static List<Question> GetAllQuestion(int lectureID) {
        List<Question> questions = new ArrayList<>();

        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("http://localhost:4000/test/lecture/" + lectureID + "/test"))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String json = response.body();

            ObjectMapper mapper = new ObjectMapper();
            questions = mapper.readValue(json, new TypeReference<List<Question>>() {
            });

        } catch (Exception e) {
            e.printStackTrace();
        }


        return questions;

    }

    public static List<Question> getQuestionForTest(int lectureID) {
        List<Question> questions = GetAllQuestion(lectureID);
        for (Question question : questions) {
            List<Map<String, Object>> answers = question.getAnswers();
            for (Map<String, Object> answerMap : answers) {
                answerMap.put("correct", false);
            }
        }
        return questions;

    }

    public static double EvaluateTest(int lectureID, List<AnswerFromClient> answers) {
        List<Question> test = GetAllQuestion(lectureID); // înlocuiește cu service/repository real

        double totalScore = 0.0;

        for (Question question : test) {
            List<Map<String, Object>> answerMaps = question.getAnswers();

            List<Integer> correctIds = answerMaps.stream()
                    .filter(a -> Boolean.TRUE.equals(a.get("correct")))
                    .map(a -> {
                        Map<String, Object> answerObj = (Map<String, Object>) a.get("answer");
                        return (Integer) answerObj.get("answerId");
                    })
                    .toList();

            int totalCorrect = correctIds.size();
            if (totalCorrect == 0) continue; // defensiv: evită împărțirea la 0

            AnswerFromClient userAnswer = answers.stream()
                    .filter(a -> a.getQuestionId() == question.getId())
                    .findFirst()
                    .orElse(null);

            if (userAnswer == null) continue;

            List<Integer> selected = userAnswer.getAnswerIds();

            int correctSelected = 0;
            int wrongSelected = 0;

            for (int id : selected) {
                if (correctIds.contains(id)) {
                    correctSelected++;
                } else {
                    wrongSelected++;
                }
            }

            double scoreForQuestion = Math.max(0, (double) (correctSelected - wrongSelected) / totalCorrect);
            totalScore += scoreForQuestion;
        }

        double grade = (totalScore / test.size()) * 10;
        return Math.round(grade * 100.0) / 100.0;
    }



    public static int sendCompleted(Completed completed) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            ObjectMapper mapper = new ObjectMapper();
            String body = mapper.writeValueAsString(completed);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("http://localhost:4000/test/completed"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200 || response.statusCode() == 201) {

                return 0;
            } else {
                System.out.println("❌ Completed creation failed. Status: " + response.statusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return -1; // eroare
    }



    public static int sendAttempt(Attempt attempt) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            ObjectMapper mapper = new ObjectMapper();
            String body = mapper.writeValueAsString(attempt);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("http://localhost:4000/test/attempt"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200 || response.statusCode() == 201) {
                int attemptId = Integer.parseInt(response.body());
                return attemptId;
            } else {
                System.out.println("❌ Attempt creation failed. Status: " + response.statusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return -1; // eroare
    }



    public static void sendAttemptInfos(List<AttemptInfo> infos) {
        System.out.println(infos);
        try {
            HttpClient client = HttpClient.newHttpClient();
            ObjectMapper mapper = new ObjectMapper();
            String body = mapper.writeValueAsString(infos);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("http://localhost:4000/test/attempt_info/batch"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200 || response.statusCode() == 201) {
                System.out.println("✅ AttemptInfo list sent.");
            } else {
                System.out.println("❌ AttemptInfo failed. Status: " + response.statusCode());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void submitAttemptFromAnswers(int userId, int lectureId, List<AnswerFromClient> answers) {
        // 1. Trimite Attempt și obține ID-ul
        Attempt attempt = new Attempt();
        attempt.setUserId(userId);
        attempt.setLectureId(lectureId);

        int attemptId = sendAttempt(attempt);
        if (attemptId == -1) {
            System.out.println("❌ Nu s-a putut crea attempt-ul.");
            return;
        }

        // 2. Transformă AnswerFromClient în AttemptInfo
        List<AttemptInfo> infos = new ArrayList<>();
        for (AnswerFromClient answer : answers) {
            for (Integer answerId : answer.getAnswerIds()) {
                AttemptInfo info = new AttemptInfo();
                info.setAttemptId(attemptId);
                info.setQuestionId(answer.getQuestionId());
                info.setAnswerId(answerId);
                infos.add(info);
            }
        }

        // 3. Trimite AttemptInfos
        sendAttemptInfos(infos);
    }
}
