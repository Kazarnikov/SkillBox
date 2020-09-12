import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity(name = "Students")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    private int age;

    private Date registration_date;

    @ManyToMany(mappedBy = "students", fetch = FetchType.LAZY)
    private List<Course> courses;

    @OneToMany(mappedBy = "studentId", orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Subscription> subscriptions;
}
