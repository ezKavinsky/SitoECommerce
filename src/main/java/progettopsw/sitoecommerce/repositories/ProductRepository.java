package progettopsw.sitoecommerce.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import progettopsw.sitoecommerce.entities.Product;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    List<Product> findByNameContaining(String name); //trova per nome che contiene la stringa passata come parametro
    List<Product> findByBarCode(String name);
    List<Product> findByBrand(String brand);
    List<Product> findByPrice(float price);
    List<Product> findByPriceBetween(float price1, float price2);
    List<Product> findByPriceLessThanEqual(float price);
    List<Product> findByPriceGreaterThanEqual(float price);
    List<Product> findByProductionYear(int year);
    List<Product> findByProductionYearAfter(int year);
    List<Product> findByProductionYearBefore(int year);
    List<Product> findByInPromoTrue();
    List<Product> findByInPromoFalse();
    List<Product> findByFreeShippingTrue();
    List<Product> findByFreeShippingFalse();
    List<Product> findByScoreGreaterThanEqualOrderByScoreDesc(int score);
    List<Product> findByScoreLessThanEqualOrderByScoreDesc(int score);
    List<Product> findByBrandAndPriceGreaterThanEqual(String brand, int price);
    List<Product> findByBrandAndPriceLessThanEqual(String brand, int price);
    List<Product> findByBrandAndInPromoTrue(String brand);
    List<Product> findByBrandAndInPromoFalse(String brand);
    boolean existsByBarCode(String barCode);
    boolean existsByBrand(String brand);
    boolean existsByProductionYear(int year);

    @Query("select p from Product p where (p.name like ?1 or ?1 is null) and (p.quantity > ?2 or ?2 is null) and (p.price > ?3 or ?3 is null)")
    List<Product> advancedSearch(String name, int quantity, int price);

    @Modifying
    @Query("update Product p set p.price = ?1 where p.barCode = ?2")
    int updatePrice(float price, String barCode);

    @Modifying
    @Query("update Product p set p.inPromo = true where p.barCode = ?1")
    int addInPromo(String barCode);

    @Modifying
    @Query("update Product p set p.inPromo = false where p.barCode = ?1")
    int removeFromPromo(String barCode);

    @Modifying
    @Query("update Product p set p.freeShipping = true where p.barCode = ?1")
    int addFreeShipping(String barCode);

    @Modifying
    @Query("update Product p set p.freeShipping = false where p.barCode = ?1")
    int removeFreeShipping(String barCode);

    @Modifying
    @Query("update Product p set p.barCode = ?1 where p.barCode = ?2")
    int updateBarCode(String barCode, String oldBarCode);

}//ProductRepository
