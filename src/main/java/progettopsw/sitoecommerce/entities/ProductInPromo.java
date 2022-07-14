package progettopsw.sitoecommerce.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "product_in_promo", schema = "orders")
public class ProductInPromo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "related_promo")
    @JsonIgnore
    private Promo promo;

    @ManyToOne
    @JoinColumn(name = "product")
    private Product product;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "related_cart")
    @JsonIgnore
    private Cart cart;

    @Basic
    @Column(name = "discount_price", nullable = false)
    private float discountPrice;


}//ProductInPromo
