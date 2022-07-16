package progettopsw.sitoecommerce.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import progettopsw.sitoecommerce.entities.Product;
import progettopsw.sitoecommerce.entities.Review;
import progettopsw.sitoecommerce.entities.User;

import java.util.Date;
import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {

    @Query("select r from Review r where r.product.id = ?1")
    List<Review> getAllByProduct(int id);

    boolean existsByProductAndBuyer(Product product, User buyer);
}//ReviewRepository
