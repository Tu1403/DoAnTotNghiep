package website.code.coffeeShop.model;

import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "Dinnertable")
public class Tables {
    @Id
    @Column(name = "table_id")
    private int tid;

    @Column(name = "number_of_chair")
    private int numberOfChair;

    @Column(name = "status")
    private int status;

    public Tables(){}

    public Tables(int tid, int numberOfChair, int status){
        this.tid = tid;
        this.numberOfChair = numberOfChair;
        this.status = status;
    }
}
