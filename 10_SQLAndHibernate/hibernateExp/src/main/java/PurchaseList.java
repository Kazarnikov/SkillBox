import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
//@IdClass(PurchaseListKey.class)
@Entity(name = "PurchaseList")
public class PurchaseList {


    @EmbeddedId
    private PurchaseListKey purchaseListKey;

   // @Id
    @Column(name = "student_name", insertable = false, updatable = false)
    private String studentName;

   // @Id
    @Column(name = "course_name", insertable = false, updatable = false)
    private String courseName;

    private int price;

    @Column(name = "subscription_date")
    private Date subscriptionDate;
}