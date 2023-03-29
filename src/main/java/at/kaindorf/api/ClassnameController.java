package at.kaindorf.api;

import at.kaindorf.database.ClassnameRepository;
import at.kaindorf.pojos.Classname;
import at.kaindorf.pojos.Student;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
@RequestMapping(value ="/api/classname",
    produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "*")
public class ClassnameController {
    private final ClassnameRepository classnameRepository;

    public ClassnameController(ClassnameRepository classnameRepository) {
        this.classnameRepository = classnameRepository;

    }

    @GetMapping("/all")
    public ResponseEntity<List>  getAllClassnames() {
        List<Classname> classnames = classnameRepository.findAll();
        classnames.sort(Comparator.comparing(Classname::getClassname));
        return ResponseEntity.ok(classnames);
        //return ResponseEntity.of(Optional.of(classnameRepository.findAll()));
    }




}
