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
@ToString
public class Classname {
    @Id
    @Column(name = "class_id")
    private Long classId;
    @Column(length = 10)
    private String classname;

    @ToString.Exclude
    @JsonIgnore
    @OneToMany(mappedBy ="classname")
    private List<Student> students;

}
