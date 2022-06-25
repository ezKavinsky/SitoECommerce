package progettopsw.sitoecommerce.entities;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.ToString;

import java.util.List;
import javax.persistence.*;

@Data
@Entity
@Table(name = "user", schema = "orders")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

    @Basic
    @Column(name = "code", nullable = true, length = 70)
    private String code;

    @Basic
    @Column(name = "first_name", nullable = true, length = 50)
    private String firstName;

    @Basic
    @Column(name = "last_name", nullable = true, length = 50)
    private String lastName;

    @Basic
    @Column(name = "telephone_number", nullable = true, length = 20)
    private String telephoneNumber;

    @Basic
    @Column(name = "email", nullable = true, length = 90)
    private String email;

    @Basic
    @Column(name = "address", nullable = true, length = 150)
    private String address;

    @OneToMany(mappedBy = "buyer", cascade = CascadeType.MERGE)
    @JsonIgnore
    private List<Purchase> purchases;

    @OneToMany(targetEntity = Review.class, mappedBy = "user", cascade = CascadeType.MERGE)
    @JsonIgnore
    @ToString.Exclude
    private List<Review> reviews;

    @OneToMany(targetEntity = CreditCard.class, mappedBy = "user", cascade = CascadeType.MERGE)
    private List<CreditCard> creditCards;

}//User
