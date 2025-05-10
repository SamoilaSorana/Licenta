package main.Sistem;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Base64;
import java.util.Map;

public class HelperFunctions {
    public static int getIdfromheader(String header) {
        try {

            // Example: "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
            String token = header.replace("Bearer ", "");

            String[] parts = token.split("\\.");
            if (parts.length < 2) {
                throw new RuntimeException("Invalid token");
            }

            String payloadJson = new String(Base64.getDecoder().decode(parts[1]));

            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> payload = mapper.readValue(payloadJson, Map.class);

            int userId = (int) payload.get("id");
            System.out.println(userId);
            return userId;


        } catch (JsonMappingException e) {
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }
}
