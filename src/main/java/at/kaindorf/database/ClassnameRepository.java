package at.kaindorf.database;

import at.kaindorf.pojos.Classname;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClassnameRepository extends JpaRepository<Classname, Long> {
}
