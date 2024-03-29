package progettopsw.sitoecommerce.controllers;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import progettopsw.sitoecommerce.entities.Cart;
import progettopsw.sitoecommerce.entities.Product;
import progettopsw.sitoecommerce.entities.Review;
import progettopsw.sitoecommerce.services.CartService;
import progettopsw.sitoecommerce.services.ProductService;
import progettopsw.sitoecommerce.services.ReviewService;
import progettopsw.sitoecommerce.support.ResponseMessage;
import progettopsw.sitoecommerce.support.exceptions.BarCodeAlreadyExistException;
import progettopsw.sitoecommerce.support.exceptions.ProductNotFoundException;
import progettopsw.sitoecommerce.support.exceptions.ReviewAlreadyExistsException;
import progettopsw.sitoecommerce.support.exceptions.UserNotFoundException;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductsController {
    @Autowired
    private ProductService productService;
    @Autowired
    private CartService cartService;
    @Autowired
    private ReviewService reviewService;

    @PostMapping
    public ResponseEntity create(@RequestBody @Valid Product product) {
        try{
            Product result = productService.addProduct(product);
            return new ResponseEntity(result,HttpStatus.CREATED);
        }catch(BarCodeAlreadyExistException e){
            return new ResponseEntity(new ResponseMessage("BARCODE_ALREADY_EXISTS"), HttpStatus.BAD_REQUEST);
        }
    }//create

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id){
        productService.removeProduct(id);
    }//delete

    @PostMapping("/{id}/cart")
    public ResponseEntity addInCart(@PathVariable String id, @RequestBody ObjectNode objectNode){
        int idC = objectNode.get("id").asInt();
        int quantity = objectNode.get("quantity").asInt();
        try{
            Cart result = cartService.addProduct(id,idC,quantity);
            return new ResponseEntity(result, HttpStatus.OK);
        } catch (ProductNotFoundException e) {
            return new ResponseEntity(new ResponseMessage("ERROR_PRODUCT_NOT_FOUND"), HttpStatus.NOT_FOUND);
        }
    }//addInCart

    @PutMapping("/{id}/updateName")
    public ResponseEntity updateName(@RequestBody String name, @PathVariable String id){
        try{
            Product product = productService.updateName(name, id);
            return new ResponseEntity(product, HttpStatus.OK);
        }catch(ProductNotFoundException e) {
            return new ResponseEntity(new ResponseMessage("ERROR_PRODUCT_NOT_FOUND"), HttpStatus.NOT_FOUND);
        }
    }//updateName

    @PutMapping("/{id}/updateBrand")
    public ResponseEntity updateBrand(@RequestBody String brand, @PathVariable String id){
        try{
            Product product = productService.updateBrand(brand, id);
            return new ResponseEntity(product, HttpStatus.OK);
        }catch(ProductNotFoundException e) {
            return new ResponseEntity(new ResponseMessage("ERROR_PRODUCT_NOT_FOUND"), HttpStatus.NOT_FOUND);
        }
    }//updateBrand

    @PutMapping("/{id}/updateBarCode")
    public ResponseEntity updateBarCode(@RequestBody String barCode, @PathVariable String id){
        try{
            Product product = productService.updateBarCode(barCode, id);
            return new ResponseEntity(product, HttpStatus.OK);
        }catch(ProductNotFoundException e) {
            return new ResponseEntity(new ResponseMessage("ERROR_PRODUCT_NOT_FOUND"), HttpStatus.NOT_FOUND);
        }
    }//updateName

    @PutMapping("/{id}/updateDescription")
    public ResponseEntity updateDescription(@RequestBody String description, @PathVariable String id){
        try{
            Product product = productService.updateDescription(description, id);
            return new ResponseEntity(product, HttpStatus.OK);
        }catch(ProductNotFoundException e) {
            return new ResponseEntity(new ResponseMessage("ERROR_PRODUCT_NOT_FOUND"), HttpStatus.NOT_FOUND);
        }
    }//updateDescription

    @PutMapping("/{id}/updatePrice")
    public ResponseEntity updatePrice(@RequestBody float price, @PathVariable String id){
        try{
            Product product = productService.updatePrice(price, id);
            return new ResponseEntity(product, HttpStatus.OK);
        }catch(ProductNotFoundException e) {
            return new ResponseEntity(new ResponseMessage("ERROR_PRODUCT_NOT_FOUND"), HttpStatus.NOT_FOUND);
        }
    }//updatePrice

    @PutMapping("/{id}/updateQuantity")
    public ResponseEntity updateQuantity(@RequestBody int quantity, @PathVariable String id){
        try{
            Product product = productService.updateQuantity(quantity, id);
            return new ResponseEntity(product, HttpStatus.OK);
        }catch(ProductNotFoundException e) {
            return new ResponseEntity(new ResponseMessage("ERROR_PRODUCT_NOT_FOUND"), HttpStatus.NOT_FOUND);
        }
    }//updateQuantity

    @PutMapping("/{id}/updateProductionYear")
    public ResponseEntity updateProductionYear(@RequestBody int year, @PathVariable String id){
        try{
            Product product = productService.updateProductionYear(year, id);
            return new ResponseEntity(product, HttpStatus.OK);
        }catch(ProductNotFoundException e) {
            return new ResponseEntity(new ResponseMessage("ERROR_PRODUCT_NOT_FOUND"), HttpStatus.NOT_FOUND);
        }
    }//updateProductionYear

    @PutMapping("/{id}/updateShippingPrice")
    public ResponseEntity updateShippingPrice(@RequestBody float price, @PathVariable String id){
        try{
            Product product = productService.updateShippingPrice(price, id);
            return new ResponseEntity(product, HttpStatus.OK);
        }catch(ProductNotFoundException e) {
            return new ResponseEntity(new ResponseMessage("ERROR_PRODUCT_NOT_FOUND"), HttpStatus.NOT_FOUND);
        }
    }//updateShippingPrice

    @PutMapping("/{id}/updateFreeShipping")
    public ResponseEntity updateFreeShipping(@RequestBody boolean cond, @PathVariable String id){
        try{
            Product product = productService.updateFreeShipping(cond, id);
            return new ResponseEntity(product, HttpStatus.OK);
        }catch(ProductNotFoundException e) {
            return new ResponseEntity(new ResponseMessage("ERROR_PRODUCT_NOT_FOUND"), HttpStatus.NOT_FOUND);
        }
    }//updateFreeShipping

    @PutMapping("/{id}/updateScore")
    public ResponseEntity updateScore(@RequestBody float score, @PathVariable String id){
        try{
            Product product = productService.updateScore(score, id);
            return new ResponseEntity(product, HttpStatus.OK);
        }catch(ProductNotFoundException e) {
            return new ResponseEntity(new ResponseMessage("ERROR_PRODUCT_NOT_FOUND"), HttpStatus.NOT_FOUND);
        }
    }//updateScore

    @GetMapping
    public List<Product> getAll(){
       return productService.showAllProducts();
    }//getAll

    @GetMapping("/paged")
    public ResponseEntity getAllPaged(@RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber,
                                 @RequestParam(value = "pageSize",defaultValue = "10") int pageSize,
                                 @RequestParam(value = "sortBy", defaultValue = "id") String sortBy){
        List<Product> result = productService.showAllProducts(pageNumber,pageSize,sortBy);
        if(result.size() <= 0){
            return new ResponseEntity(new ResponseMessage("No results!"), HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity(result,HttpStatus.FOUND);
    }//getAll

    @GetMapping("/advancedSearch")
    public List<Product> getByAdvancedSearch(@RequestParam(value = "name", defaultValue = "product") String name,
                                             @RequestParam(value = "brand", defaultValue = "brand") String brand,
                                             @RequestParam(value = "lowPrice", defaultValue = "0.0") float lowPrice,
                                             @RequestParam(value = "highPrice", defaultValue = "5000.0") float highPrice,
                                             @RequestParam(value = "lowYear", defaultValue = "2000") int lowYear,
                                             @RequestParam(value = "highYear", defaultValue = "2022") int highYear,
                                             @RequestParam(value = "freeShipping", defaultValue = "true") boolean freeShipping,
                                             @RequestParam(value = "lowScore", defaultValue = "0.0") float lowScore,
                                             @RequestParam(value = "highScore", defaultValue = "5.0") float highScore){
        return productService.showProductsByAdvancedSearch(name, brand, lowPrice, highPrice, lowYear, highYear, freeShipping, lowScore, highScore);
    }//getAll

    @GetMapping("/advancedPaged")
    public ResponseEntity getByAdvancedPagedSearch(@RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber,
                                                   @RequestParam(value = "pageSize",defaultValue = "10") int pageSize,
                                                   @RequestParam(value = "sortBy", defaultValue = "id") String sortBy,
                                                   @RequestParam(value = "name", defaultValue = "product") String name,
                                                   @RequestParam(value = "brand", defaultValue = "brand") String brand,
                                                   @RequestParam(value = "lowPrice", defaultValue = "0") float lowPrice,
                                                   @RequestParam(value = "highPrice", defaultValue = "5000") float highPrice,
                                                   @RequestParam(value = "lowYear", defaultValue = "0") int lowYear,
                                                   @RequestParam(value = "highYear", defaultValue = "2022") int highYear,
                                                   @RequestParam(value = "freeShipping", defaultValue = "true") boolean freeShipping,
                                                   @RequestParam(value = "lowScore", defaultValue = "0") float lowScore,
                                                   @RequestParam(value = "highScore", defaultValue = "5") float highScore){
        List<Product> result = productService.showProductsByAdvancedPagedSearch(pageNumber, pageSize, sortBy, name, brand, lowPrice, highPrice,
                                                                                lowYear, highYear, freeShipping, lowScore, highScore);
        if(result.size() <= 0){
            return new ResponseEntity(new ResponseMessage("No results!"), HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity(result,HttpStatus.FOUND);
    }//getByAdvancedPagedSearch

    @GetMapping("/{id}")
    public Product getProduct(@PathVariable String id){
        return productService.getProduct(id);
    }//getProduct

    @GetMapping("/search/by_name")
    public ResponseEntity getByName(@RequestParam String name) {
        List<Product> result = productService.showProductsByName(name);
        if ( result.size() <= 0 ) {
            return new ResponseEntity<>(new ResponseMessage("No results!"), HttpStatus.OK);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("{id}/reviews")
    public ResponseEntity create(@PathVariable String id, @Valid @RequestBody Review review){
        try{
            review.setProduct(productService.getProduct(id));
            Review result = reviewService.addReview(review);
            return new ResponseEntity(result, HttpStatus.CREATED);
        }catch(UserNotFoundException e){
            return new ResponseEntity(new ResponseMessage("ERROR_USER_NOT_FOUND"), HttpStatus.BAD_REQUEST);
        }catch(ProductNotFoundException e){
            return new ResponseEntity(new ResponseMessage("ERROR_PRODUCT_NOT_FOUND"), HttpStatus.BAD_REQUEST);
        }catch(ReviewAlreadyExistsException e){
            return new ResponseEntity(new ResponseMessage("ERROR_REVIEW_ALREADY_EXISTS"), HttpStatus.BAD_REQUEST);
        }
    }//create

    @GetMapping("{id}/reviews")
    public List<Review> getAll(@PathVariable String id){
        return reviewService.getAll(id);
    }//getAll

}//ProductsController
