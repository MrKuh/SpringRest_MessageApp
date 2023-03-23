package at.kaindorf.database;

import at.kaindorf.pojos.Exam;
import at.kaindorf.pojos.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ExamRepository extends JpaRepository<Exam, Long> {

    List<Exam> findAllByStudent(Student student);

    @Query("SELECT MAX(e.examId) FROM Exam e")
    Long getMaxId();

}
