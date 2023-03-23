package at.kaindorf.pojos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Subject {
    @Id
    @Column(name = "subject_id")
    private Long subjectId;
    @Column(length = 100)
    private String longname;
    @Column(length = 10)
    private String shortname;
    @Column(nullable = false)
    private boolean written;


    @ToString.Exclude
    @JsonIgnore
    @OneToMany(mappedBy ="subject")
    private List<Exam> exams;
}
