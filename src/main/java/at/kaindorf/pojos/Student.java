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
public class Student {
    @Id
    @Column(name = "student_id")
    private Long studentId;
    @Column(length = 80)
    private String firstname;
    @Column(length = 80)
    private String lastname;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "classname")
    private Classname classname;

    @ToString.Exclude
    @JsonIgnore
    @OneToMany(mappedBy ="student")
    private List<Exam> exams;

}
