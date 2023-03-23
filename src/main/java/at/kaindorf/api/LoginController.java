package at.kaindorf.api;

import at.kaindorf.database.ExamRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
@RequestMapping(value ="/login",
        produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "*")
public class LoginController {

    private ExamRepository examRepository;

    public LoginController(ExamRepository examRepository) {
        this.examRepository = examRepository;
    }

    @GetMapping("/all")
    public ResponseEntity<List> getAllExams() {
        return ResponseEntity.of(Optional.of(examRepository.findAll()));
    }
}
