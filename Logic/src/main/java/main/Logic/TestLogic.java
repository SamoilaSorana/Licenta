package main.Logic;

import Objects.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;


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
        List<Question> test = GetAllQuestion(lectureID);

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


    public static int Getcount(int user_id,int lecture_id) {
       int count =0;

        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("http://localhost:4000/test/attempt/count/"+user_id+"/"+lecture_id))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String json = response.body();
            count = Integer.parseInt(json.trim());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return count;
    }


    public static int sendHelp(Help help) {
        try {
            HttpClient client = HttpClient.newHttpClient();

            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

            String body = mapper.writeValueAsString(help);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("http://localhost:4000/test/help"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // ✅ Returnează 1 dacă a fost creat cu succes
            if (response.statusCode() == 200 || response.statusCode() == 201) {
                return 1;
            } else {
                System.out.println("❌ Help creation failed. Status: " + response.statusCode());
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


    public static List<AttemptAnswers> GetAllAttempts(int userId) {
        List<AttemptAnswers> attempts = new ArrayList<>();

        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("http://localhost:4000/test/user/" + userId + "/attempts"))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String json = response.body();

            ObjectMapper mapper = new ObjectMapper();
            attempts = mapper.readValue(json, new TypeReference<List<AttemptAnswers>>() {
            });

        } catch (Exception e) {
            e.printStackTrace();
        }


        return attempts;
    }

    public static List<Map<String, Object>> evaluateAttempts(List<AttemptAnswers> attempts) {
        List<Map<String, Object>> output = new ArrayList<>();

        for (AttemptAnswers attempt : attempts) {
            int attemptId = attempt.getAttemptId();
            int lectureId = attempt.getLectureId();
            List<Question> questions = GetAllQuestion(lectureId); // deja ai această metodă

            // Map: questionId → List<answerIds>
            Map<Integer, List<Integer>> userAnswers = attempt.getAnswers().stream()
                    .collect(Collectors.toMap(
                            AnswerFromClient::getQuestionId,
                            AnswerFromClient::getAnswerIds
                    ));

            List<Map<String, Object>> questionList = new ArrayList<>();
            double totalScore = 0;

            for (Question q : questions) {
                List<Map<String, Object>> rawAnswers = q.getAnswers(); // JSON-style
                List<Map<String, Object>> processedAnswers = new ArrayList<>();

                List<Integer> correctIds = rawAnswers.stream()
                        .filter(a -> Boolean.TRUE.equals(a.get("correct")))
                        .map(a -> (Integer) ((Map<String, Object>) a.get("answer")).get("answerId"))
                        .toList();

                List<Integer> selectedIds = userAnswers.getOrDefault(q.getId(), new ArrayList<>());
                int correctSelected = 0;
                int wrongSelected = 0;

                for (Map<String, Object> entry : rawAnswers) {
                    Map<String, Object> answer = (Map<String, Object>) entry.get("answer");
                    int aid = (Integer) answer.get("answerId");
                    String text = (String) answer.get("text");
                    boolean isCorrect = Boolean.TRUE.equals(entry.get("correct"));
                    boolean isChosen = selectedIds.contains(aid);

                    if (isCorrect && isChosen) correctSelected++;
                    if (!isCorrect && isChosen) wrongSelected++;

                    Map<String, Object> answerMap = new HashMap<>();
                    answerMap.put("answerId", aid);
                    answerMap.put("text", text);
                    answerMap.put("correct", isCorrect);
                    answerMap.put("chosenByUser", isChosen);

                    processedAnswers.add(answerMap);
                }

                double score = Math.max(0, (double)(correctSelected - wrongSelected) / correctIds.size());
                totalScore += score;

                Map<String, Object> questionMap = new HashMap<>();
                questionMap.put("questionId", q.getId());
                questionMap.put("text", q.getText());
                questionMap.put("answers", processedAnswers);
                questionList.add(questionMap);
            }

            double grade = Math.round((totalScore / questions.size()) * 10 * 100.0) / 100.0;

            Map<String, Object> result = new HashMap<>();
            result.put("attemptId", attemptId);
            result.put("lectureId", lectureId);
            result.put("grade", grade);
            result.put("questions", questionList);

            output.add(result);
        }

        return output;
    }



    public static List<Map<String,Object>> getAllEvaluatedAttempts(int userid)
    {

        List<AttemptAnswers> attempts = GetAllAttempts(userid);
        List<Map<String,Object>> evaluatedAttempts = evaluateAttempts(attempts);
        return evaluatedAttempts;
    }

}
