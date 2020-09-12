import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@Entity(name = "Subscriptions")
public class Subscription {

    @EmbeddedId
    public KeyId keyId;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", insertable = false, updatable = false)
    private Student studentId;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", insertable = false, updatable = false)
    private Course courseId;

    @Column(name = "subscription_date")
    private Date subscriptionDate;

    @Data
    @Embeddable
    @EqualsAndHashCode
    public static class KeyId implements Serializable {
        @Column(name = "student_id")
        private int studentId;

        @Column(name = "course_id")
        private int courseId;

        public KeyId() {
        }

        public KeyId(int studentId, int courseId) {
            this.studentId = studentId;
            this.courseId = courseId;
        }
    }
}
