package main.Logic;

import Objects.Acount;
import Objects.Help;
import Objects.Lecture;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class NotificationsLogic {
    public static List<Help> GetAllHelps() {
        List<Help> helpList = new ArrayList<>();

        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("http://localhost:4000/test/help"))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String json = response.body();

            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

            helpList = mapper.readValue(json, new TypeReference<List<Help>>() {});
        } catch (Exception e) {
            e.printStackTrace();
        }

        return helpList;
    }

    public static boolean updateHelpEntry(Help help) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

            String requestBody = mapper.writeValueAsString(help);

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("http://localhost:4000/test/help/" + help.getId()))
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.statusCode() == 200 || response.statusCode() == 204;

        } catch (Exception e) {
            System.err.println("❌ Failed to update help ID: " + help.getId());
            e.printStackTrace();
            return false;
        }
    }



    public static List<Help> gethelps(String authHeader) {
        List<Help> helpList = GetAllHelps();
        for(Help help: helpList) {
            help.setStudentname(GetName(help.getUserId(), authHeader));
        }
        return helpList;

    }
    public static void markHelpAsRead( List<Integer> idsToMarkRead) {
        List<Help> allHelpEntries= GetAllHelps();
        for (Help help : allHelpEntries) {
            if (idsToMarkRead.contains(help.getId())) {
                help.setRead(1);
                boolean success = updateHelpEntry(help);
                if (success) {
                    System.out.println("✅ Help ID " + help.getId() + " marked as read.");
                }
            }
        }
    }

    public static String GetName(int userID, String authHeader) {
        String name = "";


        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("http://localhost:4000/idm/user/acount/name/"+ userID))
                    .header("Authorization", authHeader)
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String json = response.body();

            ObjectMapper mapper = new ObjectMapper();
            name = mapper.readValue(json, new TypeReference<String>() {});
        } catch (Exception e) {
            e.printStackTrace();
        }

        return name;
    }


}
