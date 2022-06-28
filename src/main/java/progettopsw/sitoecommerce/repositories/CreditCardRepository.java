package progettopsw.sitoecommerce.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import progettopsw.sitoecommerce.entities.CreditCard;
import progettopsw.sitoecommerce.entities.User;

import java.util.Date;
import java.util.List;

@Repository
public interface CreditCardRepository extends JpaRepository<CreditCard, Integer> {

    CreditCard findByNumber(String number);
    CreditCard findByUser(User user);

    @Query("select c from CreditCard c where (c.expirationMonth = ?1 or ?1 is null) and (c.expirationYear = ?2)")
    List<CreditCard> findByExpirationDate(int month, int year);

    boolean existsByNumber(String number);
    boolean existsByUser(User user);

}//CreditCardRepository
