package progettopsw.sitoecommerce.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "product_in_promo_in_cart", schema = "orders")
public class ProductInPromoInCart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "related_cart")
    @JsonIgnore
    private Cart cart;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "product_in_promo")
    private ProductInPromo productInPromo;

    @Basic
    @Column(name = "quantity", nullable = false)
    private int quantity;


}//ProductInPromoInCart
