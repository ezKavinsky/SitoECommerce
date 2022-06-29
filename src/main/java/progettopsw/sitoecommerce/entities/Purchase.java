package progettopsw.sitoecommerce.entities;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "purchase", schema = "orders")
public class Purchase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

    @Basic
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "purchase_time")
    private Date purchaseTime;

    @ManyToOne
    @JoinColumn(name = "buyer")
    private User buyer;

    @ManyToOne
    @JoinColumn(name = "credit_card")
    private CreditCard creditCard;

    @OneToMany(mappedBy = "purchase", cascade = CascadeType.MERGE)
    private List<ProductInPurchase> productsInPurchase;

    @OneToMany(mappedBy = "productInPromo", cascade = CascadeType.MERGE)
    private List<ProductInPromoPurchase> productsInPromoPurchase;

    @Basic
    @JoinColumn(name = "total")
    private float total;

    @Basic
    @JoinColumn(name = "shipped")
    private boolean shipped;

}//Purchase
