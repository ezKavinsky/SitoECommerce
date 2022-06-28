package progettopsw.sitoecommerce.entities;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "promo", schema = "orders")
public class Promo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

    @Basic
    @Column(name = "name", nullable = false)
    private String name;

    @Basic
    @Column(name = "discount", nullable = false)
    private int discount;

    @Basic
    @Column(name = "start_date", nullable = false)
    private Date startDate;

    @Basic
    @Column(name = "end_date", nullable = false)
    private Date endDate;

    @OneToMany(mappedBy = "product", cascade = CascadeType.MERGE)
    private List<ProductInPromo> productsInPromo;

}//Promo
