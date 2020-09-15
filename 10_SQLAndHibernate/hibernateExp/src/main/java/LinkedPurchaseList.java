import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.io.Serializable;

@Data
@IdClass(LinkedPurchaseList.Key.class)
@Entity
@Table(name = "LinkedPurchaseList")
public class LinkedPurchaseList {

    @Id
    @Column(name = "student_id", insertable = false, updatable = false, nullable = false)
    private int studentId;
    @Id
    @Column(name = "course_id", insertable = false, updatable = false, nullable = false)
    private int courseId;

    public LinkedPurchaseList() {
    }

    public LinkedPurchaseList(int studentId, int courseId) {
        this.studentId = studentId;
        this.courseId = courseId;
    }

    @Data
    @Embeddable
    @EqualsAndHashCode
    public static class Key implements Serializable {
        @Column(name = "student_id")
        private int studentId;
        @Column(name = "course_id")
        private int courseId;
    }
}
