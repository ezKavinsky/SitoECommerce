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
    List<CreditCard> findByExpirationYear(int year);
    List<CreditCard> findByExpirationMonthAndExpirationYear(int month, int year);
    User findByUser(User user);
    boolean existsByNumber(String number);
    boolean existsByUser(User user);
    boolean existsByExpirationMonthAndExpirationYear(int month, int year);

}//CreditCardRepository
