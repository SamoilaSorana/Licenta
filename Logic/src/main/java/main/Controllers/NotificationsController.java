package main.Controllers;

import Objects.Help;
import main.Logic.LecturesLogic;
import main.Logic.NotificationsLogic;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static main.Sistem.HelperFunctions.getIdfromheader;
@RestController
@CrossOrigin(origins = "*")
public class NotificationsController {
    @PutMapping("/help")
    public ResponseEntity<?> markHelpAsRead(@RequestBody List<Integer> helpIds) {
        NotificationsLogic.markHelpAsRead( helpIds);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/help")
    public ResponseEntity<?> getallhelps(@RequestHeader("Authorization") String authHeader) {
        List<Help> helpList= NotificationsLogic.gethelps(authHeader);
        return new ResponseEntity<>(helpList, HttpStatus.OK);
    }



}
