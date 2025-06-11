package main.Controllers;

import Objects.Help;
import main.DAO.HelpDAO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
public class HelpController {

    @PostMapping("/help")
    public ResponseEntity<?> add(@RequestBody Help help) {
        boolean inserted = HelpDAO.insert(help);
        if (inserted) {
            return new ResponseEntity<>(help, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(null, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @GetMapping("/help")
    public ResponseEntity<List<Help>> getAll() {
        List<Help> helps = HelpDAO.findAll();
        return new ResponseEntity<>(helps, HttpStatus.OK);
    }

    @GetMapping("/help/{id}")
    public ResponseEntity<?> getById(@PathVariable int id) {
        Help help = HelpDAO.findById(id);
        if (help == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(help, HttpStatus.OK);
    }

    @PutMapping("/help/{id}")
    public ResponseEntity<?> update(@PathVariable int id, @RequestBody Help help) {
        help.setId(id);
        boolean updated = HelpDAO.update(help);
        if (updated) {
            return new ResponseEntity<>(help, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
