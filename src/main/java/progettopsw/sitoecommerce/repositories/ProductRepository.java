package progettopsw.sitoecommerce.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import progettopsw.sitoecommerce.entities.Product;
import org.springframework.data.domain.Pageable;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    Product findByBarCode(String name);
    boolean existsByBarCode(String barCode);

    @Query("select p from Product p where (p.name like ?1 or ?1 is null) and (p.price >= ?2 or ?2 is null) " +
            "and (p.price <= ?3 or ?3 is null) and (p.productionYear >= ?4 or ?4 is null) and (p.productionYear <= ?5 or ?5 is null) " +
            "and (p.freeShipping = ?6 or ?6 is null) and (p.score >= ?7 or ?7 is null) and (p.score <= ?8 or ?8 is null)")
    List<Product> advancedSearch(String name, String brand, int lowPrice, int highPrice, int lowYear, int highYear, boolean freeSipping,
                                        int lowScore, int highScore);

    @Query("select p from Product p where (p.name like ?1 or ?1 is null) and (p.price >= ?2 or ?2 is null) " +
            "and (p.price <= ?3 or ?3 is null) and (p.productionYear >= ?4 or ?4 is null) and (p.productionYear <= ?5 or ?5 is null) " +
            "and (p.freeShipping = ?6 or ?6 is null) and (p.score >= ?7 or ?7 is null) and (p.score <= ?8 or ?8 is null)")
    Page<Product> advancedPagedSearch(String name, String brand, int lowPrice, int highPrice, int lowYear, int highYear, boolean freeShipping,
                                             int lowScore, int highScore, Pageable paging);

}//ProductRepository
