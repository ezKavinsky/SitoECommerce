package progettopsw.sitoecommerce.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import progettopsw.sitoecommerce.entities.CreditCard;
import progettopsw.sitoecommerce.services.CreditCardService;
import progettopsw.sitoecommerce.support.ResponseMessage;
import progettopsw.sitoecommerce.support.exceptions.CreditCardNotFoundException;
import progettopsw.sitoecommerce.support.exceptions.CreditCardNumberAlreadyExistsException;
import progettopsw.sitoecommerce.support.exceptions.UserNotFoundException;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("users/{id}/creditcards")
public class CreditCardController {
    @Autowired
    private CreditCardService creditCardService;

    @PostMapping
    public ResponseEntity add(@Valid @RequestBody CreditCard creditCard, @PathVariable String id){
        try{
            CreditCard result = creditCardService.addCreditCard(creditCard,id);
            return new ResponseEntity(result, HttpStatus.CREATED);
        }catch(CreditCardNumberAlreadyExistsException e){
            return new ResponseEntity(new ResponseMessage("ERROR_CREDIT_CARD_NUMBER_ALREADY_EXISTS"), HttpStatus.BAD_REQUEST);
        } catch(CreditCardNotFoundException e) {
            return new ResponseEntity(new ResponseMessage("ERROR_CREDIT_CARD_NOT_FOUND"), HttpStatus.BAD_REQUEST);
        }catch(UserNotFoundException e){
            return new ResponseEntity(new ResponseMessage("ERROR_USER_NOT_FOUND"), HttpStatus.BAD_REQUEST);
        }
    }//add

    @DeleteMapping("/{id2}")
    public void remove(@PathVariable String id2){
        creditCardService.removeCreditCard(id2);
    }//remove

    @GetMapping("/{id2}")
    public CreditCard getCreditCard(@PathVariable String id2){
        return creditCardService.getCreditCard(id2);
    }//getByNumber

    @GetMapping
    public List<CreditCard> getByUser(@PathVariable String id){
        return creditCardService.showByUser(id);
    }//getByUser

}//CreditCardController
