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
import java.util.List;

@RestController
@RequestMapping("/productsInPromo")
public class ProductsInPromoController {
    @Autowired
    private ProductInPromoService productInPromoService;

    @PostMapping
    public ResponseEntity add(@RequestBody @Valid Product product, @RequestBody @Valid Promo promo){
        try{
            ProductInPromo productInPromo = productInPromoService.addProductInPromo(product,promo);
            return new ResponseEntity(productInPromo, HttpStatus.CREATED);
        } catch(ProductAlreadyInThisPromoException e) {
            return new ResponseEntity(new ResponseMessage("ERROR_PRODUCT_ALREADY_IN_THE_PROMOTION"), HttpStatus.BAD_REQUEST);
        } catch(ProductNotFoundException e) {
            return new ResponseEntity(new ResponseMessage("ERROR_PRODUCT_NOT_FOUND"), HttpStatus.BAD_REQUEST);
        } catch(PromoNotFoundException e) {
            return new ResponseEntity(new ResponseMessage("ERROR_PROMO_NOT_FOUND"), HttpStatus.BAD_REQUEST);
        }
    }//create

    @DeleteMapping("/{id}")
    public void remove(@PathVariable String id){
        productInPromoService.removeProductFromPromo(id);
    }//remove

    @GetMapping
    public List<ProductInPromo> getAll(){
        return productInPromoService.showAllProductsInPromo();
    }//getAll

    @GetMapping("/paged")
    public ResponseEntity getAllPaged(@RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber,
                                      @RequestParam(value = "pageSize",defaultValue = "10") int pageSize,
                                      @RequestParam(value = "sortBy", defaultValue = "id") String sortBy){
        List<ProductInPromo> result = productInPromoService.showAllProductsInPromo(pageNumber,pageSize,sortBy);
        if(result.size() <= 0){
            return new ResponseEntity(new ResponseMessage("No results!"), HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity(result,HttpStatus.FOUND);
    }//getAll

    @GetMapping
    public List<ProductInPromo> getByAdvancedSearch(@RequestParam(value = "promo", defaultValue = "null") Promo promo,
                                             @RequestParam(value = "product", defaultValue = "null") Product product){
        return productInPromoService.showProductsInPromoByAdvancedSearch(promo, product);
    }//getAll

    @GetMapping("/advancedPaged")
    public ResponseEntity getByAdvancedPagedSearch(@RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber,
                                                   @RequestParam(value = "pageSize",defaultValue = "10") int pageSize,
                                                   @RequestParam(value = "sortBy", defaultValue = "id") String sortBy,
                                                   @RequestParam(value = "promo", defaultValue = "null") Promo promo,
                                                   @RequestParam(value = "product", defaultValue = "null") Product product){
        List<ProductInPromo> result = productInPromoService.showProductsInPromoByAdvancedPagedSearch(pageNumber, pageSize, sortBy, promo, product);
        if(result.size() <= 0){
            return new ResponseEntity(new ResponseMessage("No results!"), HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity(result,HttpStatus.FOUND);
    }//getAll

}//ProductsInPromoController
