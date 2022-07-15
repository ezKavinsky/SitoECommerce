package progettopsw.sitoecommerce.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import progettopsw.sitoecommerce.entities.ProductInPromoInCart;

@Repository
public interface ProductInPromoInCartRepository extends JpaRepository<ProductInPromoInCart, Integer> {

    boolean existsByProductInPromo(int id);

}//ProductInPromoInCart
