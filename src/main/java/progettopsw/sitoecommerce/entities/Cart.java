package progettopsw.sitoecommerce.entities;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "cart", schema = "orders")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

    @OneToOne
    @Column(name = "buyer", nullable = false)
    private User buyer;

    @OneToMany(targetEntity = Product.class, cascade =CascadeType.MERGE)
    private List<Product> products;

    @OneToMany(targetEntity = ProductInPromo.class, cascade =CascadeType.MERGE)
    private List<ProductInPromo> productsInPromo;

    @Basic
    @Column(name = "total", nullable = false)
    private float total;

}//Cart
