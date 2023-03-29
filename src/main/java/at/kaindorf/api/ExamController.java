package at.kaindorf.api;


import at.kaindorf.beans.JsonExam;
import at.kaindorf.database.ExamRepository;
import at.kaindorf.database.StudentRepository;
import at.kaindorf.database.SubjectRepository;
import at.kaindorf.pojos.*;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
@RequestMapping(value ="/api/exam",
        produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "*")
public class ExamController {
    private SubjectRepository subjectRepository;
    private ExamRepository examRepository;
    private StudentRepository studentRepository;

    public ExamController(ExamRepository examRepository, StudentRepository studentRepository,
                          SubjectRepository subjectRepository) {
        this.examRepository = examRepository;
        this.studentRepository = studentRepository;
        this.subjectRepository = subjectRepository;
    }

    @GetMapping("/all")
    public ResponseEntity<List> getAllExams() {
        return ResponseEntity.of(Optional.of(examRepository.findAll()));
    }

    @GetMapping("/student/{id}")
    public ResponseEntity<List>  getExamsByStudent(@PathVariable(name = "id") Long id) {

        Optional<Student> studentOpt = studentRepository.findById(id);
        if(!studentOpt.isPresent()){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.of(Optional.of(examRepository.findAllByStudent(studentOpt.get())));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Exam> addExam(@RequestBody JsonExam jsonExam) {
        Optional<Student> studentOpt = studentRepository.findById(Long.valueOf(jsonExam.getStudentId()));
        if(!studentOpt.isPresent()){
            return ResponseEntity.notFound().build();
        }
        Optional<Subject> subjectOpt = subjectRepository.findById(Long.valueOf(jsonExam.getSubjectId()));
        if(!subjectOpt.isPresent()){
            return ResponseEntity.notFound().build();
        }
        Long examId = examRepository.getMaxId() + 1;
        Exam examToAdd = new Exam(examId,jsonExam.getDateOfExam(),jsonExam.getDuration(),studentOpt.get(),subjectOpt.get());

        examRepository.save(examToAdd);

        return ResponseEntity.status(HttpStatus.CREATED).body(examToAdd);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Exam> deleteExam(@PathVariable("id") Long examId) {
        Optional<Exam> examObj = examRepository.findById(examId);
        if (examObj.isPresent()) {
            Exam exam = examObj.get();
            examRepository.delete(exam);
            return ResponseEntity.ok(exam);
        }
        return ResponseEntity.notFound().build();
    }

    @PatchMapping()
    public ResponseEntity<Exam> patchExam(@RequestBody JsonExam jsonExam) {
        Optional<Exam> examObj = examRepository.findById(Long.valueOf(jsonExam.getExamId()));
        Optional<Subject> subjectOpt = subjectRepository.findById(Long.valueOf(jsonExam.getSubjectId() == null ? -1 : jsonExam.getSubjectId()));
        Optional<Student> studentOpt = studentRepository.findById(Long.valueOf(Long.valueOf(jsonExam.getStudentId() == null ? -1 : jsonExam.getStudentId())));

        Exam exam = examObj.get();
        Exam patch = new Exam(examObj.get().getExamId(),
                jsonExam.getDateOfExam(),
                jsonExam.getDuration(),
                studentOpt.orElse(null),
                subjectOpt.orElse(null));

        try {
            for (Field field : Exam.class.getDeclaredFields()) {
                field.setAccessible(true);
                Object value = ReflectionUtils.getField(field, patch);
                if (value != null && !value.toString().trim().isEmpty()) {
                    ReflectionUtils.setField(field, exam, value);
                }
            }
            examRepository.save(exam);
            return ResponseEntity.ok(exam);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

}
