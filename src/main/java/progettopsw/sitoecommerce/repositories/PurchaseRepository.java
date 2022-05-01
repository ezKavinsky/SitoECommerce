package progettopsw.sitoecommerce.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import progettopsw.sitoecommerce.entities.Purchase;
import progettopsw.sitoecommerce.entities.User;

import java.util.Date;
import java.util.List;

@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, Integer> {

    List<Purchase> findByBuyer(User user);
    List<Purchase> findByPurchaseTime(Date date);

    @Query("select p from Purchase p where p.purchaseTime > ?1 and p.purchaseTime < ?2 and p.buyer = ?3")
    List<Purchase> findByBuyerInPeriod(Date startDate, Date endDate, User user);

}//PurchaseRepository
