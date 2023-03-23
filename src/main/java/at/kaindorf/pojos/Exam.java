package at.kaindorf.pojos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Exam {
    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "exam_id")
    private Long examId;
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "dateofexam")
    private LocalDate dateOfExam;
    private Integer duration;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "student")
    private Student student;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "subject")
    private Subject subject;
}
