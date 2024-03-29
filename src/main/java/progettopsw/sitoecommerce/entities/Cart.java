package progettopsw.sitoecommerce.entities;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
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

    @OneToMany(targetEntity = ProductInCart.class, cascade = CascadeType.MERGE, mappedBy = "cart")
    private List<ProductInCart> products = new ArrayList<>();

    @OneToMany(targetEntity = ProductInPromoInCart.class, cascade = CascadeType.MERGE, mappedBy = "cart")
    private List<ProductInPromoInCart> productsInPromo = new ArrayList<>();

    @Basic
    @Column(name = "total", nullable = false)
    private float total;

}//Cart
