package progettopsw.sitoecommerce.entities;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "credit_card", schema = "orders")
public class CreditCard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

    @Basic
    @Column(name = "number", nullable = false)
    private String number;

    @Basic
    @Column(name = "expiration_month", nullable = false)
    private int expirationMonth;

    @Basic
    @Column(name = "expiration_year", nullable = false)
    private int expirationYear;

    @Basic
    @Column(name = "balance", nullable = false)
    @JsonIgnore
    private float balance;

    @Basic
    @Column(name = "security_code", nullable = false)
    @JsonIgnore
    private int security_code;

    @ManyToOne
    @JoinColumn(name = "user")
    private User user;

    @OneToMany(targetEntity = Purchase.class, mappedBy = "creditCard", cascade = CascadeType.MERGE)
    @JsonIgnore
    private List<Purchase> purchases;

}//CreditCard
