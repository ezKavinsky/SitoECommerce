package progettopsw.sitoecommerce.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import progettopsw.sitoecommerce.entities.*;

import java.util.List;

@Repository
public interface ProductInPromoPurchaseRepository extends JpaRepository<ProductInPromoPurchase, Integer>{

    boolean existsByPurchase(Purchase purchase);
    boolean existsByProductInPromo(ProductInPromo productInPromo);
    boolean existsByPurchaseAndProductInPromo(Purchase purchase, Product productInPromo);

    @Query("select p from ProductInPromoPurchase p where (p.purchase = ?1 or ?1 is null) and" +
            " (p.productInPromo = ?2 or ?2 is null)")
    List<ProductInPromoPurchase> advancedSearch(Purchase purchase, ProductInPromo productInPromo);

}//ProductInPromoPurchaseRepository
