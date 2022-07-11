package progettopsw.sitoecommerce.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import progettopsw.sitoecommerce.entities.CreditCard;

import java.util.List;

@Repository
public interface CreditCardRepository extends JpaRepository<CreditCard, Integer> {

    CreditCard findByNumber(String number);

    @Query("select c from CreditCard c where c.owner.id = ?1")
    List<CreditCard> findByOwner(int id);

    boolean existsByNumber(String number);

}//CreditCardRepository
