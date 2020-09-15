import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity(name = "PurchaseList")
@IdClass(PurchaseList.PurchaseListID.class)
public class PurchaseList {

    @Id
    @Column(name = "student_name", insertable = false, updatable = false)
    private String studentName;
    @Id
    @Column(name = "course_name", insertable = false, updatable = false)
    private String courseName;

    private int price;

    @Column(name = "subscription_date")
    private Date subscriptionDate;

    @Data
    @Embeddable
    @EqualsAndHashCode
    public static class PurchaseListID implements Serializable {
        @Column(name = "student_name")
        private String studentName;
        @Column(name = "course_name")
        private String courseName;
    }
}