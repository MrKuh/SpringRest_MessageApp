package at.kaindorf.database;

import at.kaindorf.pojos.Classname;
import at.kaindorf.pojos.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {

    Page<Student> findAllByClassname_ClassIdOrderByLastnameAscFirstnameAsc(Long classId, Pageable pageable);

}
