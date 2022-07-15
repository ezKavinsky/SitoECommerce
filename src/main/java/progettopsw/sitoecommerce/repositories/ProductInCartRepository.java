package progettopsw.sitoecommerce.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import progettopsw.sitoecommerce.entities.ProductInCart;

@Repository
public interface ProductInCartRepository extends JpaRepository<ProductInCart, Integer> {


}//ProductInCartRepository
