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
import java.util.List;

@Service
public class CreditCardService {
    @Autowired
    private CreditCardRepository creditCardRepository;
    @Autowired
    private UserRepository userRepository;

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public CreditCard addCreditCard(CreditCard creditCard, String id) throws CreditCardNumberAlreadyExistsException, UserNotFoundException, CreditCardNotFoundException {
        CreditCard result;
        int ident = Integer.parseInt(id);
        if(creditCardRepository.existsByNumber(creditCard.getNumber())) {
            throw new CreditCardNumberAlreadyExistsException();
        }
        if(userRepository.existsById(ident)){
            userRepository.getById(ident).getCreditCards().add(creditCard);
            result = creditCardRepository.save(creditCard);
        } else {
            throw new UserNotFoundException();
        }
        return result;
    }//addCreditCard

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void removeCreditCard(String id1){
        int ident1 = Integer.parseInt(id1);
        CreditCard creditCard = creditCardRepository.getById(ident1);
        User user = creditCard.getUser();
        user.getCreditCards().remove(creditCard);
        creditCardRepository.delete(creditCard);
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
    public CreditCard getCreditCard(String id){
        int ident =Integer.parseInt(id);
        return creditCardRepository.getById(ident);
    }//getCreditCard

    @Transactional(readOnly = true)
    public List<CreditCard> showByUser(String id){
        int ident = Integer.parseInt(id);
        return creditCardRepository.findByUser(ident);
    }//showByUser

}//CreditCardService
