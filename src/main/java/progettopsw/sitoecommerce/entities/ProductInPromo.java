package progettopsw.sitoecommerce.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
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

    @OneToMany(targetEntity = ProductInPromoPurchase.class, mappedBy = "productInPromo", cascade = CascadeType.MERGE)
    @JsonIgnore
    @ToString.Exclude
    private List<ProductInPromoPurchase> productsInPromoPurchase;

    @OneToMany(targetEntity = ProductInPromoInCart.class, mappedBy = "productInPromo", cascade = CascadeType.MERGE)
    @JsonIgnore
    @ToString.Exclude
    private List<ProductInPromoInCart> productsInPromoInCarts;

    @Basic
    @Column(name = "discount_price", nullable = false)
    private float discountPrice;


}//ProductInPromo
