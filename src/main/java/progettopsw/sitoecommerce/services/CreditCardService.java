package progettopsw.sitoecommerce.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import progettopsw.sitoecommerce.entities.CreditCard;
import progettopsw.sitoecommerce.entities.User;
import progettopsw.sitoecommerce.repositories.CreditCardRepository;
import progettopsw.sitoecommerce.repositories.UserRepository;
import progettopsw.sitoecommerce.support.exceptions.*;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class CreditCardService {
    @Autowired
    private CreditCardRepository creditCardRepository;
    @Autowired
    private UserRepository userRepository;

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void addCreditCard(CreditCard creditCard, User user) throws CreditCardNumberAlreadyExistsException, UserNotFoundException, CreditCardExpiredException, CreditCardNotFoundException {
        if(creditCardRepository.existsByNumber(creditCard.getNumber())) {
            throw new CreditCardNumberAlreadyExistsException();
        }
        if (isExpired(creditCardRepository.getById(creditCard.getId()))){
            throw new CreditCardExpiredException();
        }
        if(userRepository.existsById(user.getId())){
            userRepository.getById(user.getId()).getCreditCards().add(creditCard);
            creditCardRepository.save(creditCard);
        } else {
            throw new UserNotFoundException();
        }
    }//addCreditCard

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void removeCreditCard(CreditCard creditCard, User user) throws CreditCardNotFoundException, WrongCreditCardUserException, UserNotFoundException{
        if(!creditCardRepository.existsByNumber(creditCard.getNumber())){
            throw new CreditCardNotFoundException();
        } else {
            if(userRepository.existsById(user.getId())){
                if(userRepository.getById(user.getId()).getCreditCards().contains(creditCard)){
                    userRepository.getById(user.getId()).getCreditCards().remove(creditCard);
                    creditCardRepository.delete(creditCard);
                } else {
                    throw new WrongCreditCardUserException();
                }
            } else {
                throw new UserNotFoundException();
            }
        }
    }//removeCreditCard

    @Transactional(readOnly = true)
    public boolean isExpired(CreditCard creditCard) throws CreditCardNotFoundException{
        if(!creditCardRepository.existsByNumber(creditCard.getNumber())){
            throw new CreditCardNotFoundException();
        } else {
            Calendar now = Calendar.getInstance();
            return (now.YEAR - creditCard.getExpirationYear() >= 0) && (now.MONTH - creditCard.getExpirationMonth() >= 0);
        }
    }//checkExpirationDate

    @Transactional(readOnly = true)
    public CreditCard showByNumber(String number){
        return creditCardRepository.findByNumber(number);
    }//showByNumber

    @Transactional(readOnly = true)
    public List<CreditCard> showByUser(User user){
        return creditCardRepository.findByUser(user);
    }//showByUser

}//CreditCardService
