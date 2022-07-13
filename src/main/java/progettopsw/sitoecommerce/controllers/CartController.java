package progettopsw.sitoecommerce.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import progettopsw.sitoecommerce.entities.Cart;
import progettopsw.sitoecommerce.entities.Product;
import progettopsw.sitoecommerce.entities.ProductInPromo;
import progettopsw.sitoecommerce.services.CartService;
import progettopsw.sitoecommerce.support.ResponseMessage;
import progettopsw.sitoecommerce.support.exceptions.ProductInPromoNotFoundException;
import progettopsw.sitoecommerce.support.exceptions.ProductNotFoundException;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users/{id}/cart")
public class CartController  {
    @Autowired
    private CartService cartService;

    @PostMapping("/products")
    public ResponseEntity addProduct(@PathVariable String id, @Valid @RequestBody Product product){
        try{
            Cart result = cartService.addProduct(product, id);
            return new ResponseEntity(result, HttpStatus.OK);
        } catch(ProductNotFoundException e){
            return new ResponseEntity(new ResponseMessage("ERROR_PRODUCT_NOT_FOUND"), HttpStatus.BAD_REQUEST);
        }
    }//addProduct

    @PostMapping("/productsInPromo")
    public ResponseEntity addProductInPromo(@PathVariable String id, ProductInPromo productInPromo){
        try{
            Cart result = cartService.addProductInPromo(productInPromo, id);
            return new ResponseEntity(result, HttpStatus.OK);
        } catch(ProductInPromoNotFoundException e){
            return new ResponseEntity(new ResponseMessage("ERROR_PRODUCT_IN_PROMO_NOT_FOUND"), HttpStatus.BAD_REQUEST);
        }
    }//addProduct

    @DeleteMapping("/products")
    public void removeProduct(@PathVariable String id, @Valid @RequestBody Product product){
        cartService.removeProduct(id, product);
    }//removeProduct

    @DeleteMapping("/productsInPromo")
    public void removeProductInPromo(@PathVariable String id, @Valid @RequestBody ProductInPromo productInPromo){
        cartService.removeProductInPromo(id, productInPromo);
    }//removeProduct

    @GetMapping("/products")
    public List<Product> getProducts(@PathVariable String id){
        return cartService.getProducts(id);
    }//getProducts

    @GetMapping("/productsInPromo")
    public List<ProductInPromo> getProductsInPromo(@PathVariable String id){
        return cartService.getProductsInPromo(id);
    }//getProductsInPromo

    @GetMapping
    public Cart get(@PathVariable String id){
        return cartService.getCart(id);
    }//getCart

}//CartController
