package progettopsw.sitoecommerce.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import progettopsw.sitoecommerce.entities.Product;
import progettopsw.sitoecommerce.entities.ProductInPurchase;
import progettopsw.sitoecommerce.entities.Purchase;

import java.util.List;

@Repository
public interface ProductInPurchaseRepository extends JpaRepository<ProductInPurchase,Integer> {

    List<ProductInPurchase> findByPurchase(Purchase purchase);
    List<ProductInPurchase> findByProduct(Product product);
    List<ProductInPurchase> findByPurchaseAndProduct(Purchase purchase, Product product);
    List<ProductInPurchase> findByFinalPrice(float finalPrice);
    boolean existsByPurchase(Purchase purchase);
    boolean existsByProduct(Product product);
    boolean existByPurchaseAndProduct(Purchase purchase, Product product);

}//ProductInPurchaseRepository
