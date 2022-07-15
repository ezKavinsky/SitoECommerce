package progettopsw.sitoecommerce.controllers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import progettopsw.sitoecommerce.entities.*;
import progettopsw.sitoecommerce.services.CartService;
import progettopsw.sitoecommerce.support.ResponseMessage;
import progettopsw.sitoecommerce.support.exceptions.ProductInCartNotFoundException;
import progettopsw.sitoecommerce.support.exceptions.ProductInPromoInCartNotFoundException;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users/{id}/cart")
public class CartController  {
    @Autowired
    private CartService cartService;

    @PutMapping("/clear")
    public ResponseEntity clear(@PathVariable String id){
        Cart result = cartService.clear(id);
        return new ResponseEntity(result, HttpStatus.OK);
    }//clear

    @DeleteMapping("/products")
    public void removeProduct(@PathVariable String id, @Valid @RequestBody ProductInCart productInCart){
        cartService.removeProduct(id, productInCart);
    }//removeProduct

    @DeleteMapping("/productsInPromo")
    public void removeProductInPromo(@PathVariable String id, @Valid @RequestBody ProductInPromoInCart productInPromoInCart){
        cartService.removeProductInPromo(id, productInPromoInCart);
    }//removeProduct

    @PutMapping("/products")
    public ResponseEntity updateProductQuantity(@PathVariable String id, @RequestBody ObjectNode objectNode){
        int idP = objectNode.get("id").asInt();
        int quantity = objectNode.get("quantity").asInt();
        try{
            Cart result = cartService.updateProductQuantity(id,idP, quantity);
            return new ResponseEntity(result, HttpStatus.OK);
        } catch(ProductInCartNotFoundException e){
            return new ResponseEntity(new ResponseMessage("ERROR_PRODUCT_IN_CART_NOT_FOUND"), HttpStatus.BAD_REQUEST);
        }
    }//updateProductQuantity

    @PutMapping("/productsInPromo")
    public ResponseEntity updateProductInPromoQuantity(@PathVariable String id, @RequestBody ObjectNode objectNode){
        int idP = objectNode.get("id").asInt();
        int quantity = objectNode.get("quantity").asInt();
        try{
            Cart result = cartService.updateProductInPromoQuantity(id,idP, quantity);
            return new ResponseEntity(result, HttpStatus.OK);
        } catch(ProductInPromoInCartNotFoundException e){
            return new ResponseEntity(new ResponseMessage("ERROR_PRODUCT_IN_PROMO_IN_CART_NOT_FOUND"), HttpStatus.BAD_REQUEST);
        }
    }//updateProductQuantity

    @GetMapping("/products")
    public List<ProductInCart> getProducts(@PathVariable String id){
        return cartService.getProducts(id);
    }//getProducts

    @GetMapping("/productsInPromo")
    public List<ProductInPromoInCart> getProductsInPromo(@PathVariable String id){
        return cartService.getProductsInPromo(id);
    }//getProductsInPromo

    @GetMapping
    public Cart get(@PathVariable String id){
        return cartService.getCart(id);
    }//getCart

}//CartController