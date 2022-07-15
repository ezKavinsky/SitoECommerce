package progettopsw.sitoecommerce.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "product_in_cart", schema = "orders")
public class ProductInCart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "related_cart")
    @JsonIgnore
    private Cart cart;

    @ManyToOne
    @JoinColumn(name = "product")
    private Product product;

    @Basic
    @Column(name = "quantity", nullable = false)
    private int quantity;

}//ProductInCart
