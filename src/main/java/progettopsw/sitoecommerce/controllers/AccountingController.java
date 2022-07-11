package progettopsw.sitoecommerce.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import progettopsw.sitoecommerce.entities.User;
import progettopsw.sitoecommerce.support.ResponseMessage;
import progettopsw.sitoecommerce.support.exceptions.*;
import progettopsw.sitoecommerce.services.AccountingService;
import progettopsw.sitoecommerce.support.authentication.addUsersKeycloak;

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
            new addUsersKeycloak(user.getEmail(), "aaaa", user.getLastName());
            User added = accountingService.registerUser(user);
            return new ResponseEntity(added, HttpStatus.CREATED);
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

    @PutMapping("/{id}/updateCode")
    public ResponseEntity updateCode(@RequestBody String code, @PathVariable String id){
        try {
            User updated = accountingService.updateCode(code, id);
            return new ResponseEntity(updated, HttpStatus.OK);
        } catch(UserNotFoundException e) {
            return new ResponseEntity(new ResponseMessage("ERROR_USER_NOT_FOUND"), HttpStatus.NOT_FOUND);
        }
    }//updateCode

    @PutMapping("/{id}/updateEmail")
    public ResponseEntity updateEmail(@RequestBody String email, @PathVariable String id){
        try{
            User updated = accountingService.updateEmail(email, id);
            return new ResponseEntity(updated, HttpStatus.OK);
        }catch(UserNotFoundException e){
            return new ResponseEntity(new ResponseMessage("ERROR_USER_NOT_FOUND"), HttpStatus.NOT_FOUND);
        }
    }//updateEmail

    @PutMapping("/{id}/updateFirstName")
    public ResponseEntity updateFirstName(@RequestBody String firstName, @PathVariable String id){
        try{
            User updated = accountingService.updateFirstName(firstName, id);
            return new ResponseEntity(updated, HttpStatus.OK);
        } catch(UserNotFoundException e) {
            return new ResponseEntity(new ResponseMessage("ERROR_USER_NOT_FOUND"), HttpStatus.NOT_FOUND);
        }
    }//updateFirstName

    @PutMapping("/{id}/updateLastName")
    public ResponseEntity updateLastName(@RequestBody String lastName, @PathVariable String id){
        try{
            User updated = accountingService.updateLastName(lastName, id);
            return new ResponseEntity(updated, HttpStatus.OK);
        }catch (UserNotFoundException e){
            return new ResponseEntity(new ResponseMessage("ERROR_USER_NOT_FOUND"), HttpStatus.NOT_FOUND);
        }
    }//updateLastName

    @PutMapping("/{id}/updateTelephoneNumber")
    public ResponseEntity updateTelephoneNumber(@RequestBody String telephoneNumber, @PathVariable String id){
        try{
            User updated = accountingService.updateTelephoneNumber(telephoneNumber, id);
            return new ResponseEntity(updated, HttpStatus.OK);
        }catch(UserNotFoundException e) {
            return new ResponseEntity(new ResponseMessage("ERROR_USER_NOT_FOUND"), HttpStatus.NOT_FOUND);
        }
    }//updateTelephoneNumber

    @PutMapping("/{id}/updateAddress")
    public ResponseEntity updateAddress(@RequestBody String address, @PathVariable String id){
        try{
            User updated = accountingService.updateAddress(address, id);
            return new ResponseEntity(updated, HttpStatus.OK);
        }catch(UserNotFoundException e){
            return new ResponseEntity(new ResponseMessage("ERROR_USER_NOT_FOUND"), HttpStatus.NOT_FOUND);
        }
    }//updateAddress

    @PutMapping("/{id}/updateBirthDate")
    public ResponseEntity updateBirthDate(@RequestBody Date birthDate, @PathVariable String id){
        try{
            User updated = accountingService.updateBirthDate(birthDate, id);
            return new ResponseEntity(updated, HttpStatus.OK);
        }catch(UserNotFoundException e){
            return new ResponseEntity(new ResponseMessage("ERROR_USER_NOT_FOUND"), HttpStatus.NOT_FOUND);
        }
    }//updateBirthDate

    @GetMapping("/{id}")
    public User getUser(@PathVariable String id){
       return accountingService.getUser(id);
    }//getUser

    @GetMapping("/getByAdvancedSearch")
    public List<User> getByAdvancedSearch( @RequestParam (value = "firstName" , defaultValue = "null") String firstName,
                                           @RequestParam (value = "lastName", defaultValue = "null") String lastName,
                                           @RequestParam (value = "telephoneNumber", defaultValue = "null") String telephoneNumber,
                                           @RequestParam (value = "address", defaultValue = "null") String address) {
        return accountingService.showUsersByAdvancedSearch(firstName, lastName, telephoneNumber, address);
    }//getByAdvancedSearch

}//AccountingController
