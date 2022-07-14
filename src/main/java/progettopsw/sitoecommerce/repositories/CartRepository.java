package progettopsw.sitoecommerce.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import progettopsw.sitoecommerce.entities.Cart;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> {

    @Query("select c from Cart c where c.buyer.id = ?1")
    Cart findByBuyer(int id);

}//CartRepository
