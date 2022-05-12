package progettopsw.sitoecommerce.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import progettopsw.sitoecommerce.entities.User;
import progettopsw.sitoecommerce.exceptions.MailUserAlreadyExistsException;
import progettopsw.sitoecommerce.services.AccountingService;

import java.util.List;

@RestController
@RequestMapping("/users")
public class AccountingController {

    @Autowired
    private AccountingService accountingService;

    @PostMapping
    public ResponseEntity create(@RequestBody User user){
        try{
            User added = accountingService.registerUser(user);
            return new ResponseEntity(added, HttpStatus.OK);
        }catch(MailUserAlreadyExistsException e){new ResponseMessage("ERROR_MAIL_USER_ALREADY_EXISTS"), HttpStatus.BAD_REQUEST};
    }

    @GetMapping
    public List<User> getAll(){
        return accountingService.getAllUsers();
    }

}//AccountingController