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

    List<Review> findByTitle(String title);
    List<Review> findByStars(int stars);
    List<Review> findByProduct(Product product);
    List<Review> findByUser(User user);
    List<Review> findByStarsGreaterThanEqual(int stars);
    List<Review> findByStarsLessThanEqual(int stars);
    List<Review> findByStarsBetween(int stars1, int stars2);
    List<Review> findByStarsAndProduct(int stars, Product product);
    List<Review> findByUserAndProduct(User user, Product product);
    boolean existsByTitle(String title);
    boolean existsByStars(int stars);
    boolean existsByUser(User user);
    boolean existsByProduct(Product product);
    boolean existsByUserAndProduct(User user, Product product);

}//ReviewRepository
