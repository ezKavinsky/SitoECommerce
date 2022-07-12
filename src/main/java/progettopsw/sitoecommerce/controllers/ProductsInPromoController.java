package progettopsw.sitoecommerce.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import progettopsw.sitoecommerce.entities.Product;
import progettopsw.sitoecommerce.entities.ProductInPromo;
import progettopsw.sitoecommerce.entities.Promo;
import progettopsw.sitoecommerce.services.ProductInPromoService;
import progettopsw.sitoecommerce.support.ResponseMessage;
import progettopsw.sitoecommerce.support.exceptions.ProductAlreadyInThisPromoException;
import progettopsw.sitoecommerce.support.exceptions.ProductNotFoundException;
import progettopsw.sitoecommerce.support.exceptions.PromoNotFoundException;

import javax.validation.Valid;
import javax.ws.rs.Path;
import java.util.List;

@RestController
@RequestMapping("/promos/{id}/productsInPromo")
public class ProductsInPromoController {
    @Autowired
    private ProductInPromoService productInPromoService;

    @PostMapping
    public ResponseEntity add(@RequestBody @Valid Product product, @PathVariable String id){
        try{
            ProductInPromo productInPromo = productInPromoService.addProductInPromo(product,id);
            return new ResponseEntity(productInPromo, HttpStatus.CREATED);
        } catch(ProductAlreadyInThisPromoException e) {
            return new ResponseEntity(new ResponseMessage("ERROR_PRODUCT_ALREADY_IN_THE_PROMOTION"), HttpStatus.BAD_REQUEST);
        } catch(ProductNotFoundException e) {
            return new ResponseEntity(new ResponseMessage("ERROR_PRODUCT_NOT_FOUND"), HttpStatus.BAD_REQUEST);
        } catch(PromoNotFoundException e) {
            return new ResponseEntity(new ResponseMessage("ERROR_PROMO_NOT_FOUND"), HttpStatus.BAD_REQUEST);
        }
    }//add

    @DeleteMapping("/{id2}")
    public void remove(@PathVariable String id2){
        productInPromoService.removeProductFromPromo(id2);
    }//remove

    @GetMapping("/paged")
    public ResponseEntity getAllPaged(@RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber,
                                      @RequestParam(value = "pageSize",defaultValue = "10") int pageSize,
                                      @RequestParam(value = "sortBy", defaultValue = "id") String sortBy,
                                      @PathVariable String id){
        try {
            List<ProductInPromo> result = productInPromoService.showProductsInPromoPaged(pageNumber, pageSize, sortBy, id);
            if (result.size() <= 0) {
                return new ResponseEntity(new ResponseMessage("No results!"), HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity(result, HttpStatus.FOUND);
        }catch(PromoNotFoundException e){
            return new ResponseEntity(new ResponseMessage("ERROR_PROMO_NOT_FOUND"), HttpStatus.NOT_FOUND);
        }
    }//getAllPaged

    @GetMapping()
    public List<ProductInPromo> getByPromo(@PathVariable String id){
        return productInPromoService.showProductsInPromo(id);
    }//getByAdvancedSearch

    @GetMapping("/{id2}")
    public ProductInPromo getProductInPromo(@PathVariable String id2){
        return productInPromoService.getProductInPromo(id2);
    }//getProductInPromo

}//ProductsInPromoController
