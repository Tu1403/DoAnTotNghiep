package website.code.coffeeShop.model;

import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "Product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int pid;

    @Column(name = "pname")
    private String pname;

    @Column(name = "description")
    private String description;

    @Column(name = "unit")
    private String unit;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "price")
    private float price;

    @Column(name = "image")
    private String image;

    @Column(name = "category_id")
    private int categoryId;

    @Transient
    private String categoryName;

    public Product() {}

    public Product(String pname, String description, String unit, int quantity, float price, String image, int categoryId) {
        this.pname = pname;
        this.description = description;
        this.unit = unit;
        this.quantity = quantity;
        this.price = price;
        this.image = image;
        this.categoryId = categoryId;
    }
}
