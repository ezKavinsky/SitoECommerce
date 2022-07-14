package progettopsw.sitoecommerce.entities;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "cart", schema = "orders")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

    @OneToOne(mappedBy = "cart")
    @JsonIgnore
    private User buyer;

    @OneToMany(targetEntity = Product.class, cascade = CascadeType.MERGE, mappedBy = "cart")
    private List<Product> products = new ArrayList<>();

    @OneToMany(targetEntity = ProductInPromo.class, cascade = CascadeType.MERGE, mappedBy = "cart")
    private List<ProductInPromo> productsInPromo = new ArrayList<>();

    @Basic
    @Column(name = "total", nullable = false)
    private float total;

}//Cart
