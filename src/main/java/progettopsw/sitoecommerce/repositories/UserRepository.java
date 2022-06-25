package progettopsw.sitoecommerce.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import progettopsw.sitoecommerce.entities.User;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    List<User> findByFirstName(String firstName);
    List<User> findByLastName(String lastName);
    List<User> findByFirstNameAndLastName(String firstName, String lastName);
    List<User> findByEmail(String email);
    List<User> findByCode(String code);
    boolean existsByEmail(String email);
    boolean existsByCode(String code);
    boolean existsByTelephoneNumber(String telephoneNumber);


}//UserRepository
