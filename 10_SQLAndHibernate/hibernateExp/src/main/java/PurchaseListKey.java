import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Getter
@Setter
@Embeddable
@EqualsAndHashCode
public class PurchaseListKey  implements Serializable {
    @Column(name = "student_name")
    private String studentName;
    @Column(name = "course_name")
    private String courseName;

    public PurchaseListKey() {
    }

    public PurchaseListKey(String studentName, String courseName) {
        this.studentName = studentName;
        this.courseName = courseName;
    }
}