package progettopsw.sitoecommerce.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import progettopsw.sitoecommerce.entities.ProductInPromo;
import progettopsw.sitoecommerce.entities.Promo;
import progettopsw.sitoecommerce.services.PromotingService;
import progettopsw.sitoecommerce.support.ResponseMessage;
import progettopsw.sitoecommerce.support.exceptions.PromoAlreadyExistsException;
import progettopsw.sitoecommerce.support.exceptions.PromoNotFoundException;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/promos")
public class PromotingController {
    @Autowired
    private PromotingService promotingService;

    @GetMapping("/{id}")
    public Promo getPromo(@PathVariable String id){
        return promotingService.getPromo(id);
    }//promo

    @PostMapping
    public ResponseEntity create(@Valid @RequestBody Promo promo){
        try{
            promotingService.addPromo(promo);
            return new ResponseEntity(promo, HttpStatus.CREATED);
        } catch(PromoAlreadyExistsException e){
            return new ResponseEntity(new ResponseMessage("ERROR_PROMO_ALREADY_EXISTS"), HttpStatus.BAD_REQUEST);
        }
    }//create

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id){
        promotingService.removePromo(id);
    }//delete

    @GetMapping("/{id}/getProducts")
    public List<ProductInPromo> getProducts(@PathVariable String id) throws PromoNotFoundException{
        try {
            return promotingService.getProductsInPromo(id);
        } catch(PromoNotFoundException e){
            throw new PromoNotFoundException();
        }
    }//getProducts

    @GetMapping("/{id}/products")
    public ResponseEntity getProductsPaged(@PathVariable String id, @RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber,
                                           @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                           @RequestParam(value = "sortBy", defaultValue = "id") String sortBy){
        List<ProductInPromo> result = promotingService.getProductsInPromoPaged(pageNumber, pageSize, sortBy, id);
        if(result.size() <= 0) {
            return new ResponseEntity(new ResponseMessage("No content!"), HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity(result, HttpStatus.FOUND);
    }//getProductsPaged


    @GetMapping
    public List<Promo> getAll(){
        return promotingService.showAllPromos();
    }//promo

}//PromotingController
