package main.Controllers;

import Objects.AttemptInfo;
import main.DAO.AttemptInfoDAO;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AttemptInfoController {

    @PostMapping("/attempt_info")
    public ResponseEntity<?> add(@RequestBody AttemptInfo info) {
        boolean inserted = AttemptInfoDAO.insert(info);
        if (inserted) {
            return new ResponseEntity<>(info, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(null, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @PostMapping("/attempt_info/batch")
    public ResponseEntity<?> addMultiple(@RequestBody List<AttemptInfo> infoList) {
        boolean inserted = AttemptInfoDAO.insert(infoList);
        if (inserted) {
            return new ResponseEntity<>(infoList, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(null, HttpStatus.UNPROCESSABLE_ENTITY);
    }
}
