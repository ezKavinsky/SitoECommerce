package progettopsw.sitoecommerce.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import progettopsw.sitoecommerce.entities.Product;
import progettopsw.sitoecommerce.entities.ProductInPromo;
import progettopsw.sitoecommerce.entities.Promo;

import org.springframework.data.domain.Pageable;
import java.util.List;

@Repository
public interface ProductInPromoRepository extends JpaRepository<ProductInPromo, Integer> {

    @Query("select p from ProductInPromo p where (p.product.name like concat(?1, '%')or ?1 is null)")
    List<ProductInPromo> advancedSearch(Product product);

    @Query("select p from ProductInPromo p where (p.promo = ?1)")
    Page<ProductInPromo> advancedPagedSearch(Promo promo, Pageable paging);

    boolean existsByProductAndPromo(Product product, Promo promo);

}//ProductInPromoRepository
