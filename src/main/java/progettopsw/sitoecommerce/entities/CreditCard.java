package progettopsw.sitoecommerce.entities;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import javax.persistence.*;
import java.util.List;

@Data
@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
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
    @Column(name = "security_code", nullable = false)
    @JsonIgnore
    private int security_code;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "owner")
    private User owner;

    @OneToMany(targetEntity = Purchase.class, mappedBy = "creditCard", cascade = CascadeType.MERGE)
    @JsonIgnore
    private List<Purchase> purchases;

}//CreditCard
