package main.Logic;

import Objects.Acount;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ProfileLogic {
    public static Acount GetAcount(int userID, String authHeader) {
        Acount acount = null;

        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("http://localhost:4000/idm/user/acount/"+ userID))
                    .header("Authorization", authHeader)
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String json = response.body();

            ObjectMapper mapper = new ObjectMapper();
            acount = mapper.readValue(json, new TypeReference<Acount>() {});
        } catch (Exception e) {
            e.printStackTrace();
        }

        return acount;
    }

}
