package progettopsw.sitoecommerce.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import progettopsw.sitoecommerce.entities.ProductInPurchase;

@Repository
public interface ProductInPurchaseRepository extends JpaRepository<ProductInPurchase,Integer> {


}//ProductInPurchaseRepository
