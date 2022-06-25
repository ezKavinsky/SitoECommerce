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
    private int number;

    @Basic
    @Temporal(TemporalType.DATE)
    @Column(name = "expiration_date", nullable = false)
    private Date expiration_date;

    @Basic
    @Column(name = "security_code", nullable = false)
    private int security_code;

    @ManyToOne
    @JoinColumn(name = "user")
    private User user;

}//CreditCard
