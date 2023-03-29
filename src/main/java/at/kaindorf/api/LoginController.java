package at.kaindorf.api;

import at.kaindorf.database.ExamRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
@RequestMapping(value ="/api/login",
        produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "*")
public class LoginController{

    private ExamRepository examRepository;

    public LoginController(ExamRepository examRepository) {
        this.examRepository = examRepository;
    }

    @GetMapping
    public ResponseEntity sendToPage(){
        String url = "https://google.com";

        URI uri= null;
        try {
            uri = new URI(url);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).location(uri).build();
    }

    @GetMapping("/all")
    public ResponseEntity<List> getAllExams() {
        return ResponseEntity.of(Optional.of(examRepository.findAll()));
    }
}
