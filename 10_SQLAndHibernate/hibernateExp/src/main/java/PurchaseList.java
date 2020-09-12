import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
//@IdClass(PurchaseList.PurchaseListID.class)
@Entity(name = "PurchaseList")
public class PurchaseList {

    @EmbeddedId
    private PurchaseListID PurchaseListID;

    @Column(name = "student_name", insertable = false, updatable = false)
    private String studentName;

    @Column(name = "course_name", insertable = false, updatable = false)
    private String courseName;

    private int price;

    @Column(name = "subscription_date")
    private Date subscriptionDate;

    @Data
    @Embeddable
    @EqualsAndHashCode
    public class PurchaseListID implements Serializable {
        @Column(name = "student_name")
        private String studentName;
        @Column(name = "course_name")
        private String courseName;

        public PurchaseListID() {
        }

        public PurchaseListID(String studentName, String courseName) {
            this.studentName = studentName;
            this.courseName = courseName;
        }
    }
}