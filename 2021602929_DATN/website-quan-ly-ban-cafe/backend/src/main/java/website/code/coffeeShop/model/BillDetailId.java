package website.code.coffeeShop.model;

import javax.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@Embeddable
public class BillDetailId implements Serializable {
    @Column(name = "bill_id")
    private int billId;

    @Column(name = "pid")
    private int pid;

    public BillDetailId() {}
    public BillDetailId(int billId, int pid) {
        this.billId = billId;
        this.pid = pid;
    }
}
