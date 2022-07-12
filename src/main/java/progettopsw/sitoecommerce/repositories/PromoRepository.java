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



}//PromoRepository
