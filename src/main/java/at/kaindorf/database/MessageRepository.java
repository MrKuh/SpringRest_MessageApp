package at.kaindorf.database;

import at.kaindorf.pojos.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MessageRepository extends JpaRepository<Message, Long> {

    @Query("SELECT MAX(m.messageId) FROM Message m")
    Long getMaxId();

}
