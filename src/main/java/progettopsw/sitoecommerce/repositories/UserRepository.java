package progettopsw.sitoecommerce.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import progettopsw.sitoecommerce.entities.Product;
import progettopsw.sitoecommerce.entities.User;

import java.util.Date;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    User findByEmail(String email);
    User findByCode(String code);
    boolean existsByEmail(String email);
    boolean existsByCode(String code);

    @Query("select u from User u where (u.firstName = ?1 or ?1 is null) and (u.lastName = ?2 or ?2 is null) and (u.telephoneNumber = ?3 or ?3 is null)" +
            "and (u.address = ?4 or ?4 is null) and (u.birthDate >= ?5 or ?5 is null) and (u.birthDate <= ?6 or ?6 is null) " +
            "and (u.registrationDate >= ?7 or ?7 is null) and (u.registrationDate <= ?8 or ?8 is null) and (u.status = ?9 or ?9 is null)")
    List<User> advancedSearch(String firstName, String lastName, String telephoneNumber, String address, Date startBDate, Date endBDate, Date startRDate,
                                  Date endRDate, String status);

}//UserRepository
