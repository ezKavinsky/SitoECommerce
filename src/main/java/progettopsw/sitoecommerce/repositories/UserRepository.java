package progettopsw.sitoecommerce.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import progettopsw.sitoecommerce.entities.User;

import java.util.Date;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    List<User> findByFirstName(String firstName);
    List<User> findByLastName(String lastName);
    List<User> findByFirstNameAndLastName(String firstName, String lastName);
    User findByEmail(String email);
    User findByCode(String code);
    List<User> findByTelephoneNumber(String telephoneNumber);
    List<User> findByAddress(String address);
    List<User> findByRegistrationDate(Date date);
    List<User> findByRegistrationDateBefore(Date date);
    List<User> findByRegistrationDateAfter(Date date);
    List<User> findByRegistrationDateBetween(Date startDate, Date endDate);
    List<User> findByBirthDate(Date date);
    List<User> findByBirthDateBefore(Date date);
    List<User> findByBirthDateAfter(Date date);
    List<User> findByBirthDateBetween(Date startDate, Date endDate);
    boolean existsByEmail(String email);
    boolean existsByCode(String code);
    boolean existsByTelephoneNumber(String telephoneNumber);
    boolean existsByAddress(String address);
    boolean existsByLastName(String lastName);
    boolean existsByBirthDate(Date date);
    boolean existsByRegistrationDate(Date date);

}//UserRepository
