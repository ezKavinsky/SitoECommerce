package progettopsw.sitoecommerce.entities;
import lombok.Data;
import javax.persistence.*;
import java.util.Date;

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
    @Temporal(TemporalType.DATE)
    @Column(name = "expiration_month", nullable = false)
    private int expirationMonth;

    @Basic
    @Temporal(TemporalType.DATE)
    @Column(name = "expiration_year", nullable = false)
    private int expirationYear;

    @Basic
    @Column(name = "security_code", nullable = false)
    private int security_code;

    @ManyToOne
    @JoinColumn(name = "user")
    private User user;

}//CreditCard
