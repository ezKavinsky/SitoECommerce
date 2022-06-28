package progettopsw.sitoecommerce.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import progettopsw.sitoecommerce.entities.Product;
import progettopsw.sitoecommerce.entities.Review;
import progettopsw.sitoecommerce.entities.User;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {

    boolean existsByUser(User user);
    boolean existsByProduct(Product product);
    boolean existsByUserAndProduct(User user, Product product);

    @Query("select r from Review r where (r.title = ?1 or ?1 is null) and (r.stars >= ?2 or ?2 is null) and (r.stars <= ?3 or ?3 is null) " +
            "and (r.product = ?4 or ?4 is null) and (r.user = ?5 or ?5 is null)")
    List<Review> advancedSearch(String title, int lowStars, int highStars, Product product, User user);

}//ReviewRepository