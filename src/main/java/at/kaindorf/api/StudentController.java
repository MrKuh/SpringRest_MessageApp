package at.kaindorf.api;

import at.kaindorf.database.ClassnameRepository;
import at.kaindorf.database.StudentRepository;
import at.kaindorf.pojos.Classname;
import at.kaindorf.pojos.Student;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
@RestController
@Slf4j
@RequestMapping(value ="/api/student",
        produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "*")
public class StudentController {
    private final StudentRepository studentRepository;
    private final ClassnameRepository classnameRepository;
    private final ClassnameRepository StudentController;

    public StudentController(ClassnameRepository classnameRepository, ClassnameRepository studentController,
                             StudentRepository studentRepository) {
        this.classnameRepository = classnameRepository;
        StudentController = studentController;
        this.studentRepository = studentRepository;
    }

    @GetMapping("/all")
    public ResponseEntity<List> getAllStudents() {
        return ResponseEntity.of(Optional.of(studentRepository.findAll()));
    }

    @GetMapping("/classname/{id}")
    public ResponseEntity<Page>  getStudentsByClassname(
            @PathVariable(name = "id") Long id,
            @RequestParam(name = "pageNo", defaultValue = "0") int pageNo,
            @RequestParam(name = "pageSize", defaultValue = "10") int pageSize) {

        /*
        Optional<Classname> classOpt = classnameRepository.findById(id);

        if(!classOpt.isPresent()){
            return ResponseEntity.notFound().build();
        }

         */

        Pageable page = PageRequest.of(pageNo, pageSize,
                Sort.by("lastname").descending());

        return ResponseEntity.of(Optional.of(studentRepository.findAllByClassname_ClassIdOrderByLastnameAscFirstnameAsc(id, page)));
    }
}
