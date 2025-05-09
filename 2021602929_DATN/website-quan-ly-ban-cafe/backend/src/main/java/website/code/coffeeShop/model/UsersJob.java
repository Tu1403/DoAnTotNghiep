package website.code.coffeeShop.model;

import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "Users")
public class UsersJob {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "uid")
    private int uid;

    @Column(name = "fullname")
    private String fullname;

    @Column(name = "dob")
    private String dob;

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phone;

    @Column(name = "address")
    private String address;

    @Column(name = "avatar")
    private String avatar;

    @Column(name = "username")
    private String username;

    @Column(name = "pass")
    private String pass;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Roles role;

    @Column(name = "status")
    private int status;

    public UsersJob() {}

    public UsersJob(int uid, String fullname, String dob, String email, String phone, String address, String avatar, String username, String pass, Roles role, int status) {
        this.uid = uid;
        this.fullname = fullname;
        this.dob = dob;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.avatar = avatar;
        this.username = username;
        this.pass = pass;
        this.role = role;
        this.status = status;
    }
}
