package progettopsw.sitoecommerce.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;

@Data
@Entity
@Table(name = "product_in_promo", schema = "orders")
public class ProductInPromo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

    @ManyToOne
    @JoinColumn(name = "related_promo")
    @JsonIgnore
    @ToString.Exclude
    private Promo promo;

    @ManyToOne
    @JoinColumn(name = "product")
    private Product product;

    @Basic
    @Column(name = "discount_price", nullable = false)
    private float discountPrice;


}//ProductInPromo
