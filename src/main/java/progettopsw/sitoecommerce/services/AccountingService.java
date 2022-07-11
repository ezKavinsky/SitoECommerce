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
    public void deleteUser(String id){
        int ident = Integer.parseInt(id);
        if(userRepository.existsById(ident)){
            userRepository.delete(userRepository.getById(ident));
        }
    }//deleteUser

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public User updateCode(String code, String id) throws UserNotFoundException {
        User result;
        int ident = Integer.parseInt(id);
        if(userRepository.existsById(ident)){
            result = userRepository.getById(ident);
            result.setCode(code);
        } else {
            throw new UserNotFoundException();
        }
        return result;
    }//updateCode

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public User updateEmail(String email, String id) throws UserNotFoundException {
        User result;
        int ident = Integer.parseInt(id);
        if(userRepository.existsById(ident)){
            result = userRepository.getById(ident);
            result.setEmail(email);
        } else {
            throw new UserNotFoundException();
        }
        return result;
    }//updateEmail

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public User updateFirstName(String firstName, String id) throws UserNotFoundException {
        User result;
        int ident = Integer.parseInt(id);
        if(userRepository.existsById(ident)){
            result = userRepository.getById(ident);
            result.setFirstName(firstName);
        } else {
            throw new UserNotFoundException();
        }
        return result;
    }//updateFirstName

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public User updateLastName(String lastName, String id) throws UserNotFoundException {
        User result;
        int ident = Integer.parseInt(id);
        if(userRepository.existsById(ident)){
            result = userRepository.getById(ident);
            result.setLastName(lastName);
        } else {
            throw new UserNotFoundException();
        }
        return result;
    }//UpdateLastName

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public User updateTelephoneNumber(String telephoneNumber, String id) throws UserNotFoundException {
        User result;
        int ident = Integer.parseInt(id);
        if(userRepository.existsById(ident)){
            result = userRepository.getById(ident);
            result.setTelephoneNumber(telephoneNumber);
        } else {
            throw new UserNotFoundException();
        }
        return result;
    }//updateTelephoneNumber

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public User updateAddress(String address, String id) throws UserNotFoundException {
        User result;
        int ident = Integer.parseInt(id);
        if(userRepository.existsById(ident)){
            result = userRepository.getById(ident);
            result.setAddress(address);
        } else {
            throw new UserNotFoundException();
        }
        return result;
    }//updateAddress

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public User updateBirthDate(Date birthDate, String id) throws UserNotFoundException {
        User result;
        int ident = Integer.parseInt(id);
        if(userRepository.existsById(ident)){
            result = userRepository.getById(ident);
            result.setBirthDate(birthDate);
        } else {
            throw new UserNotFoundException();
        }
        return result;
    }//updateBirthDate

    @Transactional(readOnly = true)
    public User showUserByEmail(String email){
        return userRepository.findByEmail(email);
    }//showUserByEmail

    @Transactional(readOnly = true)
    public User showUserByCode(String code){
        return userRepository.findByCode(code);
    }//showUserByCode

    @Transactional(readOnly = true)
    public User getUser(String id){
        int ident = Integer.parseInt(id);
        return userRepository.getById(ident);
    }//getUser

    @Transactional(readOnly = true)
    public List<User> showUsersByAdvancedSearch(String firstName, String lastName, String telephoneNumber, String address){
        return userRepository.advancedSearch(firstName, lastName, telephoneNumber, address);
    }//showUsersByAdvancedSearch



}//AccountingService
