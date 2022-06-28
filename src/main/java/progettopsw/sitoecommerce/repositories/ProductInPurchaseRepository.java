package progettopsw.sitoecommerce.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import progettopsw.sitoecommerce.entities.Product;
import progettopsw.sitoecommerce.entities.ProductInPurchase;
import progettopsw.sitoecommerce.entities.Purchase;

import java.util.List;

@Repository
public interface ProductInPurchaseRepository extends JpaRepository<ProductInPurchase,Integer> {

    boolean existsByPurchase(Purchase purchase);
    boolean existsByProduct(Product product);
    boolean existByPurchaseAndProduct(Purchase purchase, Product product);

    @Query("select p from ProductInPurchase p where (p.purchase = ?1 or ?1 is null) and (p.product = ?2 or ?2 is null)")
    List<ProductInPurchase> advancedSearch(Purchase purchase, Product product);

}//ProductInPurchaseRepository
