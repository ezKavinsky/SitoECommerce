package progettopsw.sitoecommerce.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import progettopsw.sitoecommerce.entities.Promo;

import java.util.Date;
import java.util.List;

@Repository
public interface PromoRepository extends JpaRepository<Promo, Integer> {

    Promo findByName(String name);

    @Query("select p from Promo p where (p.startDate >= ?1 or ?1 is null) and (p.endDate <= ?2 or ?2 is null)")
    List<Promo> findByDate(Date startDate, Date endDate);

}//PromoRepository
