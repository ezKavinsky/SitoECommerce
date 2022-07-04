package progettopsw.sitoecommerce.controllers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import progettopsw.sitoecommerce.entities.ProductInPromoPurchase;
import progettopsw.sitoecommerce.entities.ProductInPurchase;
import progettopsw.sitoecommerce.entities.Purchase;
import progettopsw.sitoecommerce.services.PurchasingService;
import progettopsw.sitoecommerce.support.ResponseMessage;
import progettopsw.sitoecommerce.support.exceptions.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@RestController
@RequestMapping("/purchases")
public class PurchasingController {
    @Autowired
    private PurchasingService purchasingService;

    @PostMapping
    public ResponseEntity add(@Valid @RequestBody Purchase purchase){
        try{
            Purchase result = purchasingService.addPurchase(purchase);
            return new ResponseEntity(result, HttpStatus.CREATED);
        }catch(QuantityProductUnavailableException e){
            return new ResponseEntity(new ResponseMessage("ERROR_QUANTITY_PRODUCT_UNAVAILABLE"), HttpStatus.BAD_REQUEST);
        }catch(CreditCardNotFoundException e) {
            return new ResponseEntity(new ResponseMessage("ERROR_CREDIT_CARD_NOT_FOUND"), HttpStatus.BAD_REQUEST);
        }catch (PurchaseAlreadyExistsException e){
            return new ResponseEntity(new ResponseMessage("ERROR_PURCHASE_ALREADY_EXISTS"), HttpStatus.BAD_REQUEST);
        }
    }//add

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id){
        try {
            purchasingService.cancelPurchase(id);
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

    @GetMapping("/{id}/advancedSearch")
    public List<Purchase> getByAdvancedSearch(@PathVariable String id, @RequestBody Date startDate, Date endDate, boolean shipped){
        try{
            List<Purchase> result = purchasingService.getPurchaseByAdvancedSearch(id, startDate, endDate, shipped);
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

    @GetMapping("/{id}/products")
    public List<ProductInPurchase> getProducts(@PathVariable String id){
        return purchasingService.getProductsByPurchase(id);
    }//getProducts

    @GetMapping("/{id}/productsInPromo")
    public List<ProductInPromoPurchase> getProductsInPromo(@PathVariable String id){
        return purchasingService.getProductsInPromoByPurchase(id);
    }//getProducts

    @GetMapping("/{id}")
    public Purchase getPurchase(@PathVariable String id){
        return purchasingService.getPurchase(id);
    }//getPurchase

}//PurchasingController
