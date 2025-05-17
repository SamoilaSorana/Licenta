package main.Controllers;

import Objects.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import main.Logic.LecturesLogic;
import main.Logic.ProfileLogic;
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
public class ProfileController {
    @GetMapping("/user/get-info")
    public ResponseEntity<?> getUserInfo(@RequestHeader("Authorization") String authHeader) {
        int id = getIdfromheader(authHeader);
        Acount acount = ProfileLogic.GetAcount(id,authHeader);
        return new ResponseEntity<>(acount, HttpStatus.OK);
    }

}
