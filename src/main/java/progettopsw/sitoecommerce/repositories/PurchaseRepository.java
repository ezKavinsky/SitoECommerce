package progettopsw.sitoecommerce.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import progettopsw.sitoecommerce.entities.Product;
import progettopsw.sitoecommerce.entities.Purchase;
import progettopsw.sitoecommerce.entities.User;

import java.util.Date;
import java.util.List;

@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, Integer> {

    List<Purchase> findByBuyer(User user);
    List<Purchase> findByPurchaseTime(Date date);
    List<Purchase> findByPurchaseTimeBetween(Date startDate, Date endDate);
    List<Purchase> findByPurchaseTimeAfter(Date date);
    List<Purchase> findByPurchaseTimeBefore(Date date);
    List<Purchase> findByProductsInPurchaseContaining(Product product);



    @Query("select p from Purchase p where p.purchaseTime > ?1 and p.purchaseTime < ?2 and p.buyer = ?3")
    List<Purchase> findByBuyerInPeriod(Date startDate, Date endDate, User user);


}//PurchaseRepository
