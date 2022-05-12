package progettopsw.sitoecommerce.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import progettopsw.sitoecommerce.entities.User;
import progettopsw.sitoecommerce.support.exceptions.MailUserAlreadyExistsException;
import progettopsw.sitoecommerce.repositories.UserRepository;
import java.util.List;

@Service
public class AccountingService {
    @Autowired
    private UserRepository userRepository;

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public User registerUser(User user) throws MailUserAlreadyExistsException {
        if(userRepository.existsByEmail(user.getEmail())){
            throw new MailUserAlreadyExistsException();
        }
        return userRepository.save(user);
    }//registerUser

    @Transactional(readOnly = true)
    public List<User> getAllUsers() {return userRepository.findAll();}

}//AccountingService
