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

    @Query("select p from Purchase p where (p.purchaseTime >= ?1 or ?1 is null) and (p.purchaseTime <= ?2 or ?2 is null) and (p.buyer = ?3 or ?3 is null) " +
            "and (p.shipped = ?4 or ?4 is null)")
    List<Purchase> advancedSearch(Date startDate, Date endDate, User user, boolean shipped);


}//PurchaseRepository
