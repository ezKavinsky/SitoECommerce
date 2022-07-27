package progettopsw.sitoecommerce.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.ToString;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "product", schema = "orders")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

    @Basic
    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Basic
    @Column(name = "brand", nullable=false, length = 50)
    private String brand;

    @Basic
    @Column(name = "bar_code", nullable = false, length = 70)
    private String barCode;

    @Basic(fetch = FetchType.LAZY)
    @Column(name = "description", nullable = false, length = 500)
    private String description;

    @Basic
    @Column(name = "price", nullable = false)
    private float price;

    @Basic
    @Column(name = "quantity", nullable = false)
    private int quantity;

    @Basic(fetch = FetchType.LAZY)
    @Column(name = "production_year", nullable = false)
    private int productionYear;

    @Basic
    @Column(name = "free_shipping", nullable = false)
    private boolean freeShipping;

    @Basic
    @Column(name = "shipping_price", nullable = true)
    private float shippingPrice;

    @Basic(fetch = FetchType.LAZY)
    @Column(name = "score", nullable = true)
    private float score;

    @Basic
    @Column(name = "version", nullable = false)
    @JsonIgnore
    private long version;

    @OneToMany(targetEntity = ProductInPurchase.class, mappedBy = "product", cascade = CascadeType.MERGE)
    @ToString.Exclude
    @JsonIgnore
    private List<ProductInPurchase> productsInPurchase = new ArrayList<>();

    @OneToMany(targetEntity = ProductInCart.class, mappedBy = "product", cascade = CascadeType.MERGE)
    @ToString.Exclude
    @JsonIgnore
    private List<ProductInCart> productsInCarts = new ArrayList<>();

    @OneToMany(targetEntity = Review.class, mappedBy = "product", cascade = CascadeType.MERGE)
    @JsonIgnore
    private List<Review> reviews = new ArrayList<>();

}//Product
