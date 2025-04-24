package website.code.coffeeShop.model;

import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "Roles")
public class Roles {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private int roleId;

    @Column(name = "role_name")
    private String roleName;

    public Roles() {}
    public Roles(int roleId, String roleName) {
        this.roleId = roleId;
        this.roleName = roleName;
    }
}
