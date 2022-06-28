package progettopsw.sitoecommerce.services;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import progettopsw.sitoecommerce.entities.User;
import progettopsw.sitoecommerce.support.exceptions.CodeUserAlreadyExistsException;
import progettopsw.sitoecommerce.support.exceptions.MailUserAlreadyExistsException;
import progettopsw.sitoecommerce.repositories.UserRepository;
import progettopsw.sitoecommerce.support.exceptions.UserNotFoundException;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class AccountingService {
    @Autowired
    private UserRepository userRepository;

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public User registerUser(User user) throws MailUserAlreadyExistsException, CodeUserAlreadyExistsException {
        if(userRepository.existsByEmail(user.getEmail())){
            throw new MailUserAlreadyExistsException();
        } else if(userRepository.existsByCode(user.getCode())){
            throw new CodeUserAlreadyExistsException();
        }
        return userRepository.save(user);
    }//registerUser

    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }//getAllUsers

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void deleteUser(User user) throws UserNotFoundException {
        if(userRepository.existsById(user.getId())){
            userRepository.delete(user);
        } else {
            throw new UserNotFoundException();
        }
    }//deleteUser

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void updateCode(String code, User user) throws UserNotFoundException {
        if(userRepository.existsById(user.getId())){
            userRepository.getById(user.getId()).setCode(code);
        } else {
            throw new UserNotFoundException();
        }
    }//updateCode

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void updateEmail(String email, User user) throws UserNotFoundException {
        if(userRepository.existsById(user.getId())){
            userRepository.getById(user.getId()).setEmail(email);
        } else {
            throw new UserNotFoundException();
        }
    }//updateEmail

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void updateFirstName(String firstName, User user) throws UserNotFoundException {
        if(userRepository.existsById(user.getId())){
            userRepository.getById(user.getId()).setFirstName(firstName);
        } else {
            throw new UserNotFoundException();
        }
    }//updateFirstName

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void updateLastName(String lastName, User user) throws UserNotFoundException {
        if(userRepository.existsById(user.getId())){
            userRepository.getById(user.getId()).setLastName(lastName);
        } else {
            throw new UserNotFoundException();
        }
    }//UpdateLastName

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void updateTelephoneNumber(String telephoneNumber, User user) throws UserNotFoundException {
        if(userRepository.existsById(user.getId())){
            userRepository.getById(user.getId()).setTelephoneNumber(telephoneNumber);
        } else {
            throw new UserNotFoundException();
        }
    }//updateTelephoneNumber

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void updateAddress(String address, User user) throws UserNotFoundException {
        if(userRepository.existsById(user.getId())){
            userRepository.getById(user.getId()).setAddress(address);
        } else {
            throw new UserNotFoundException();
        }
    }//updateAddress

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void updateBirthDate(Date birthDate, User user) throws UserNotFoundException {
        if(userRepository.existsById(user.getId())){
            userRepository.getById(user.getId()).setBirthDate(birthDate);
        } else {
            throw new UserNotFoundException();
        }
    }//updateBirthDate

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void updateStatus(User user) throws UserNotFoundException{
        if(userRepository.existsById(user.getId())){
            Calendar today = Calendar.getInstance();
            if(today.getTime().compareTo(userRepository.getById(user.getId()).getRegistrationDate()) > 4){
                userRepository.getById(user.getId()).setStatus("FIDELITY_USER");
            } else if(today.getTime().compareTo(userRepository.getById(user.getId()).getRegistrationDate()) < 1){
                userRepository.getById(user.getId()).setStatus("NEW_USER");
            } else {
                userRepository.getById(user.getId()).setStatus("NORMAL_USER");
            }
        } else {
            throw new UserNotFoundException();
        }
    }//updateStatus

    @Transactional(readOnly = true)
    public User showUserByEmail(String email){
        return userRepository.findByEmail(email);
    }//showUserByEmail

    @Transactional(readOnly = true)
    public User showUserByCode(String code){
        return userRepository.findByCode(code);
    }//showUserByCode

    @Transactional(readOnly = true)
    public List<User> showUsersByAdvancedSearch(String firstName, String lastName, String telephoneNumber, String address, Date startBDate, Date endBdate,
                                                Date startRDate, Date endRDate, String status){
        return userRepository.advancedSearch(firstName, lastName, telephoneNumber, address, startBDate, endBdate, startRDate, endRDate, status);
    }//showUsersByAdvancedSearch

}//AccountingService
