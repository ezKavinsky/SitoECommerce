package progettopsw.sitoecommerce.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.ToString;
import javax.persistence.*;
import java.util.List;

@Data
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
    @Column(name = "brand", nullable=false)
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
    private int production_year;

    @Basic
    @Column(name = "inPromo", nullable = false)
    private boolean inPromo;

    @Basic
    @Column(name = "freeShipping", nullable = false)
    private boolean freeShipping;

    @Basic(fetch = FetchType.LAZY)
    @Column(name = "score", nullable = true)
    private double score;

    @Basic
    @Column(name = "version", nullable = false)
    private long version;

    @OneToMany(targetEntity = ProductInPurchase.class, mappedBy = "product", cascade = CascadeType.MERGE)
    @JsonIgnore
    @ToString.Exclude
    private List<ProductInPurchase> productsInPurchase;

}//Product
