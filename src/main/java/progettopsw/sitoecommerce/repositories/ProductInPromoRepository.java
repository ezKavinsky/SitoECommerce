package progettopsw.sitoecommerce.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import progettopsw.sitoecommerce.entities.Product;
import progettopsw.sitoecommerce.entities.ProductInPromo;
import progettopsw.sitoecommerce.entities.Promo;

import java.util.List;

@Repository
public interface ProductInPromoRepository extends JpaRepository<ProductInPromo, Integer> {

    @Query("select p from ProductInPromo p where (p.promo = ?1 or ?1 is null) and (p.product = ?2 or ?2 is null) and (p.discountPrice = ?3 or ?3 is null)")
    List<ProductInPromo> advancedSearch(Promo promo, Product product, int discountPrice);

}//ProductInPromoRepository
