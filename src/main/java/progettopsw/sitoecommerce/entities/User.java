package progettopsw.sitoecommerce.entities;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.*;

@Data
@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "user", schema = "orders")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

    @Basic
    @Column(name = "code", nullable = false, length = 70)
    private String code;

    @Basic
    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;

    @Basic
    @Column(name = "last_name", nullable = false, length = 50)
    private String lastName;

    @Basic
    @Column(name = "telephone_number", nullable = false, length = 20)
    private String telephoneNumber;

    @Basic
    @Column(name = "email", nullable = false, length = 90)
    private String email;

    @Basic
    @Column(name = "address", nullable = false, length = 150)
    private String address;

    @Basic
    @Column(name = "birth_date", nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date birthDate;

    @Basic
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "registration_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date registrationDate;

    @OneToMany(targetEntity = Purchase.class, mappedBy = "buyer", cascade = CascadeType.MERGE)
    @JsonIgnore
    private List<Purchase> purchases;

    @OneToMany(targetEntity = Review.class, mappedBy = "buyer", cascade = CascadeType.MERGE)
    @JsonIgnore
    @ToString.Exclude
    private List<Review> reviews = new ArrayList<>();

    @OneToMany(targetEntity = CreditCard.class, mappedBy = "owner", cascade = CascadeType.MERGE)
    @JsonIgnore
    private List<CreditCard> creditCards;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "cart_id")
    @JsonIgnore
    private Cart cart;

}//User
