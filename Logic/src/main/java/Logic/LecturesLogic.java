package Logic;


import Objects.Lecture;

import java.util.List;

public class LecturesLogic {
    public static List<Lecture> GetLectures(){
    try {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("https://api.example.com/data"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println("Status code: " + response.statusCode());
        System.out.println("Response body: " + response.body());
    } catch (Exception e) {
        e.printStackTrace();
    }
}

