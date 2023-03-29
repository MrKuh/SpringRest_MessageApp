package at.kaindorf.api;

import at.kaindorf.database.ClassnameRepository;
import at.kaindorf.database.SubjectRepository;
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
@RequestMapping(value ="/api/subject",
        produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "*")
public class SubjectController {

    private final SubjectRepository subjectRepository;

    public SubjectController(SubjectRepository subjectRepository) {
        this.subjectRepository = subjectRepository;

    }

    @GetMapping("/all")
    public ResponseEntity<List> getAllSubjects() {
        return ResponseEntity.of(Optional.of(subjectRepository.findAll()));
    }


}
