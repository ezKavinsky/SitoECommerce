package progettopsw.sitoecommerce.controllers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import progettopsw.sitoecommerce.entities.*;
import progettopsw.sitoecommerce.services.PurchasingService;
import progettopsw.sitoecommerce.support.ResponseMessage;
import progettopsw.sitoecommerce.support.exceptions.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@RestController
@RequestMapping("/users/{id}/purchases")
public class PurchasingController {
    @Autowired
    private PurchasingService purchasingService;

    @PostMapping
    public ResponseEntity add(@Valid @RequestBody Cart cart, @PathVariable String id){
        try{
            Purchase result = purchasingService.addPurchase(cart, id);
            return new ResponseEntity(result, HttpStatus.CREATED);
        }catch(QuantityProductUnavailableException e){
            return new ResponseEntity(new ResponseMessage("ERROR_QUANTITY_PRODUCT_UNAVAILABLE"), HttpStatus.BAD_REQUEST);
        }catch(CreditCardNotFoundException e) {
            return new ResponseEntity(new ResponseMessage("ERROR_CREDIT_CARD_NOT_FOUND"), HttpStatus.BAD_REQUEST);
        }
    }//add

    @DeleteMapping("/{id2}")
    public void delete(@PathVariable String id2){
        try {
            purchasingService.cancelPurchase(id2);
        }catch(PurchaseAlreadyShippedException e){}
    }//delete

    @GetMapping
    public List<Purchase> getAll(@PathVariable String id){
        try {
            return purchasingService.getPurchaseByUser(id);
        }catch(UserNotFoundException e){
            return new ArrayList<>();
        }
    }//getAll

    @GetMapping("/{id2}/advancedSearch")
    public List<Purchase> getByAdvancedSearch(@PathVariable String id2, @RequestBody Date startDate, Date endDate, boolean shipped){
        try{
            List<Purchase> result = purchasingService.getPurchaseByAdvancedSearch(id2, startDate, endDate, shipped);
            if(result.size() <= 0){
                return new ArrayList<>();
            } else {
                return result;
            }
        }catch(UserNotFoundException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found!", e);
        }catch(DateWrongRangeException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Date not valid!", e);
        }
    }//getByAdvancedSearch

    @GetMapping("/{id2}/products")
    public List<ProductInPurchase> getProducts(@PathVariable String id){
        return purchasingService.getProductsByPurchase(id);
    }//getProducts

    @GetMapping("/{id2}/productsInPromo")
    public List<ProductInPromoPurchase> getProductsInPromo(@PathVariable String id){
        return purchasingService.getProductsInPromoByPurchase(id);
    }//getProducts

    @GetMapping("/{id2}")
    public Purchase getPurchase(@PathVariable String id){
        return purchasingService.getPurchase(id);
    }//getPurchase

}//PurchasingController
