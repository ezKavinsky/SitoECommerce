package progettopsw.sitoecommerce.controllers;

import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import progettopsw.sitoecommerce.entities.User;
import progettopsw.sitoecommerce.support.ResponseMessage;
import progettopsw.sitoecommerce.support.exceptions.CodeUserAlreadyExistsException;
import progettopsw.sitoecommerce.support.exceptions.MailUserAlreadyExistsException;
import progettopsw.sitoecommerce.services.AccountingService;
import progettopsw.sitoecommerce.support.exceptions.UserNotFoundException;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/users")
public class AccountingController {

    @Autowired
    private AccountingService accountingService;

    @PostMapping
    public ResponseEntity create(@RequestBody @Valid User user){
        try{
            User added = accountingService.registerUser(user);
            return new ResponseEntity(added, HttpStatus.OK);
        }catch(MailUserAlreadyExistsException e){
            return new ResponseEntity(new ResponseMessage("ERROR_MAIL_USER_ALREADY_EXISTS"), HttpStatus.BAD_REQUEST);
        }catch(CodeUserAlreadyExistsException e){
            return new ResponseEntity(new ResponseMessage("ERROR_CODE_USER_ALREADY_EXISTS"), HttpStatus.BAD_REQUEST);
        }
    }//create

    @GetMapping
    public List<User> getAll(){
        return accountingService.getAllUsers();
    }//getAll

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id){
        accountingService.deleteUser(id);
    }//delete

    @PutMapping("/{id}")
    public ResponseEntity updateCode(@RequestBody String code, @PathVariable String id){
        try {
            User updated = accountingService.updateCode(code, id);
            return new ResponseEntity(updated, HttpStatus.OK);
        } catch(UserNotFoundException e) {
            return new ResponseEntity(new ResponseMessage("ERROR_USER_NOT_FOUND"), HttpStatus.NOT_FOUND);
        }
    }//updateCode

    @PutMapping("/{id}")
    public ResponseEntity updateEmail(@RequestBody String email, @PathVariable String id){
        try{
            User updated = accountingService.updateEmail(email, id);
            return new ResponseEntity(updated, HttpStatus.OK);
        }catch(UserNotFoundException e){
            return new ResponseEntity(new ResponseMessage("ERROR_USER_NOT_FOUND"), HttpStatus.NOT_FOUND);
        }
    }//updateEmail

    @PutMapping("/{id}")
    public ResponseEntity updateFirstName(@RequestBody String firstName, @PathVariable String id){
        try{
            User updated = accountingService.updateFirstName(firstName, id);
            return new ResponseEntity(updated, HttpStatus.OK);
        } catch(UserNotFoundException e) {
            return new ResponseEntity(new ResponseMessage("ERROR_USER_NOT_FOUND"), HttpStatus.NOT_FOUND);
        }
    }//updateFirstName

    @PutMapping("/{id}")
    public ResponseEntity updateLastName(@RequestBody String lastName, @PathVariable String id){
        try{
            User updated = accountingService.updateLastName(lastName, id);
            return new ResponseEntity(updated, HttpStatus.OK);
        }catch (UserNotFoundException e){
            return new ResponseEntity(new ResponseMessage("ERROR_USER_NOT_FOUND"), HttpStatus.NOT_FOUND);
        }
    }//updateLastName

    @PutMapping("/{id}")
    public ResponseEntity updateTelephoneNumber(@RequestBody String telephoneNumber, @PathVariable String id){
        try{
            User updated = accountingService.updateTelephoneNumber(telephoneNumber, id);
            return new ResponseEntity(updated, HttpStatus.OK);
        }catch(UserNotFoundException e) {
            return new ResponseEntity(new ResponseMessage("ERROR_USER_NOT_FOUND"), HttpStatus.NOT_FOUND);
        }
    }//updateTelephoneNumber

    @PutMapping("/{id}")
    public ResponseEntity updateAddress(@RequestBody String address, @PathVariable String id){
        try{
            User updated = accountingService.updateAddress(address, id);
            return new ResponseEntity(updated, HttpStatus.OK);
        }catch(UserNotFoundException e){
            return new ResponseEntity(new ResponseMessage("ERROR_USER_NOT_FOUND"), HttpStatus.NOT_FOUND);
        }
    }//updateAddress

    @PutMapping("/{id}")
    public ResponseEntity updateBirthDate(@RequestBody Date birthDate, @PathVariable String id){
        try{
            User updated = accountingService.updateBirthDate(birthDate, id);
            return new ResponseEntity(updated, HttpStatus.OK);
        }catch(UserNotFoundException e){
            return new ResponseEntity(new ResponseMessage("ERROR_USER_NOT_FOUND"), HttpStatus.NOT_FOUND);
        }
    }//updateBirthDate

    @GetMapping
    public User getByEmail(@RequestBody String email){
       return accountingService.showUserByEmail(email);
    }//showByEmail

    @GetMapping
    public User getByCode(@RequestBody String code){
        return accountingService.showUserByCode(code);
    }//showByCode

    @GetMapping
    public List<User> getByAdvancedSearch(@RequestBody String firstName, @RequestBody String lastName, @ RequestBody String telephoneNumber,
                                              @RequestBody String address, @RequestBody Date startBDate, @RequestBody Date endBdate,
                                              @RequestBody Date startRDate, @RequestBody Date endRDate) {
        return accountingService.showUsersByAdvancedSearch(firstName, lastName, telephoneNumber, address,
                startBDate, endBdate, startRDate, endRDate);
    }//getByAdvancedSearch

}//AccountingController
