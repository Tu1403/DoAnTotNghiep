package website.code.coffeeShop.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@Table(name = "Feedback")
public class Feedback {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(name = "fid")
    private int fid;

    @Column(name = "user_id")
    private int userId;

    @Column(name = "bill_id")
    private int billId;

    @Column(name = "created_time")
    private Date createdTime;

    @Column(name = "status")
    private int status;

    @Column(name = "complain")
    private String complain;

    @Column(name = "respon")
    private String respon;
}
