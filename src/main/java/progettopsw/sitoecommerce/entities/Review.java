package progettopsw.sitoecommerce.entities;

import lombok.Data;
import javax.persistence.*;

@Data
@Entity
@Table(name = "review", schema = "orders")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

    @Basic
    @Column(name = "title", nullable = false, length = 50)
    private String title;

    @Basic
    @Column(name = "comment", nullable = true, length = 200)
    private String comment;

    @Basic
    @Column(name = "stars", nullable = false)
    private float stars;

  @ManyToOne(cascade = CascadeType.MERGE)
  @JoinColumn(name = "product")
    private Product product;

  @ManyToOne(cascade = CascadeType.MERGE)
  @JoinColumn(name = "user")
  private User user;

}//Review
