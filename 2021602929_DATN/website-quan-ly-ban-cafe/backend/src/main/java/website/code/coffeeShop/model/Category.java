package website.code.coffeeShop.model;

import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "Category")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int cid;
    @Column(name = "group_name")
    private String groupName;
    @Column(name = "category_name")
    private String categoryName;
    @Column(name = "description")
    private String describe;

    public Category() {}

    public Category(String groupName, String categoryName, String describe) {
        this.groupName = groupName;
        this.categoryName = categoryName;
        this.describe = describe;
    }

    public Category(int cid, String groupName, String categoryName, String describe) {
        this.cid = cid;
        this.groupName = groupName;
        this.categoryName = categoryName;
        this.describe = describe;
    }
}
