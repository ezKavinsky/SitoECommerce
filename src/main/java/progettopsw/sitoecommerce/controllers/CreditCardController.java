package progettopsw.sitoecommerce.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import progettopsw.sitoecommerce.entities.CreditCard;
import progettopsw.sitoecommerce.services.CreditCardService;
import progettopsw.sitoecommerce.support.ResponseMessage;
import progettopsw.sitoecommerce.support.exceptions.CreditCardNumberAlreadyExistsException;

@RestController
@RequestMapping("/creditcards")
public class CreditCardController {
    @Autowired
    private CreditCardService creditCardService;

    @PostMapping
    public ResponseEntity add(CreditCard creditCard){
        try{
            CreditCard result = creditCardService.addCreditCard(creditCard);
            return new ResponseEntity(result, HttpStatus.CREATED);
        }catch(CreditCardNumberAlreadyExistsException e){
            return new ResponseEntity(new ResponseMessage("ERROR_CREDIT_CARD_NUMBER_ALREADY_EXISTS"), HttpStatus.BAD_REQUEST);
        }
    }//add

    @DeleteMapping("/{id}")
    public void remove(@PathVariable String id){
        creditCardService.removeCreditCard(id);
    }//remove



}//CreditCardController
