package progettopsw.sitoecommerce.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
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
            "and (u.address = ?4 or ?4 is null)")
    List<User> advancedSearch(String firstName, String lastName, String telephoneNumber, String address);

}//UserRepository
