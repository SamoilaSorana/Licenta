package main.Logic;

import Objects.Chapter;
import Objects.Completed;
import Objects.Lecture;
import Objects.RequiredLecture;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.checkerframework.checker.units.qual.C;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;
import java.util.stream.Collectors;

public class LecturesLogic {

    public static List<Lecture> GetAllLectures() {
        List<Lecture> lectures = new ArrayList<>();

        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("http://localhost:4000/test/lecture/all"))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String json = response.body();

            ObjectMapper mapper = new ObjectMapper();
            lectures = mapper.readValue(json, new TypeReference<List<Lecture>>() {});
        } catch (Exception e) {
            e.printStackTrace();
        }

        return lectures;
    }

    public static List<Completed> GetCompletedLectures(int userID) {
        List<Completed> completedList = new ArrayList<>();

        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("http://localhost:4000/test/completed/user/" + userID))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String json = response.body();

            ObjectMapper mapper = new ObjectMapper();
            completedList = mapper.readValue(json, new TypeReference<List<Completed>>() {});
        } catch (Exception e) {
            e.printStackTrace();
        }

        return completedList;
    }

    public static List<RequiredLecture> GetRequiredLectures() {
        List<RequiredLecture> requiredLectures = new ArrayList<>();

        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("http://localhost:4000/test/required_lecture"))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String json = response.body();

            ObjectMapper mapper = new ObjectMapper();
            requiredLectures = mapper.readValue(json, new TypeReference<List<RequiredLecture>>() {});
        } catch (Exception e) {
            e.printStackTrace();
        }

        return requiredLectures;
    }




    public static List<Lecture> getLecturesByChapterId(int chapterId) {
        List<Lecture> lectures = new ArrayList<>();

        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("http://localhost:4000/test/lecture/chapter/" + chapterId))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String json = response.body();

            ObjectMapper mapper = new ObjectMapper();
            lectures = mapper.readValue(json, new TypeReference<List<Lecture>>() {});
        } catch (Exception e) {
            e.printStackTrace();
        }

        return lectures;
    }


    public static List<Chapter> GetAllChapters() {
        List<Chapter> chapters = new ArrayList<>();

        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("http://localhost:4000/test/chapters/all"))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String json = response.body();

            ObjectMapper mapper = new ObjectMapper();
            chapters = mapper.readValue(json, new TypeReference<List<Chapter>>() {});
        } catch (Exception e) {
            e.printStackTrace();
        }

        return chapters;
    }


    public static List<Map<String, Object>> mapChaptersWithLectures(List<Chapter> chapters, List<Lecture> lectures) {

        Map<Integer, List<Lecture>> lecturesByChapterId = lectures.stream()
                .filter(l -> l.getChapter() != null)
                .collect(Collectors.groupingBy(l -> l.getChapter().getId()));


        List<Map<String, Object>> result = new ArrayList<>();

        for (Chapter chapter : chapters) {
            List<Lecture> chapterLectures = lecturesByChapterId.get(chapter.getId());
            if (chapterLectures != null && !chapterLectures.isEmpty()) {
                Map<String, Object> map = new HashMap<>();
                map.put("chapter", chapter);
                map.put("lectures", chapterLectures);
                result.add(map);
            }
        }

        return result;
    }
    public static void removeCompletedLectures(List<Lecture> lectures, List<Completed> completedList) {
        Set<Integer> completedIds = completedList.stream()
                .map(Completed::getLectureId)
                .collect(Collectors.toSet());

        lectures.removeIf(lecture -> completedIds.contains(lecture.getLectureId()));
    }

    public static void filterLockedLectures(
            List<Lecture> lectures,
            List<Completed> completedList,
            List<RequiredLecture> requirements
    ) {
        Set<Integer> completedIds = completedList.stream()
                .map(Completed::getLectureId)
                .collect(Collectors.toSet());

        lectures.removeIf(lecture -> {
            int lectureId = lecture.getLectureId();

            List<RequiredLecture> prereqs = requirements.stream()
                    .filter(req -> req.getCurrentLectureId() == lectureId)
                    .collect(Collectors.toList());

            return prereqs.stream()
                    .anyMatch(req -> !completedIds.contains(req.getRequiredLectureId()));
        });
    }

    public static List<Map<String,Object>> GetLectures(int userId) {
        List<Lecture> lectures = GetAllLectures();
        List<Completed> completedList = GetCompletedLectures(userId);
        List<RequiredLecture> requiredLectures = GetRequiredLectures();

        removeCompletedLectures(lectures, completedList);
        filterLockedLectures(lectures, completedList, requiredLectures);

        List<Chapter> chapters =GetAllChapters();

      List<Map<String,Object>>groupedLectures= mapChaptersWithLectures(chapters, lectures);

        return groupedLectures;
    }


    public static List<Map<String,Object>> GetLecturesWithoutFilter(int userId) {
        List<Lecture> lectures = GetAllLectures();

        List<Chapter> chapters =GetAllChapters();

        List<Map<String,Object>>groupedLectures= mapChaptersWithLectures(chapters, lectures);

        return groupedLectures;
    }


    public static Map<String,Object> GetLectureCount(int userId) {
        List<Lecture> lectures = GetAllLectures();
        Map<String, Object> result = new HashMap<>();
        List<Completed> completedList = GetCompletedLectures(userId);
        int total = lectures.size();
        int completed = completedList.size();


        result.put("completed", completed);
        result.put("total", total);

        return result;
    }



}
