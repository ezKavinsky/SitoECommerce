package progettopsw.sitoecommerce.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import progettopsw.sitoecommerce.entities.Product;
import progettopsw.sitoecommerce.services.ProductService;
import progettopsw.sitoecommerce.support.ResponseMessage;
import progettopsw.sitoecommerce.support.exceptions.BarCodeAlreadyExistException;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductsController {

    @Autowired
    private ProductService productService;

    @PostMapping
    public ResponseEntity create(@RequestBody @Valid Product product) {
        try{
            productService.addProduct(product);
        }catch(BarCodeAlreadyExistException e){
            return new ResponseEntity(new ResponseMessage("BARCODE_ALREADY_EXISTS"), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(new ResponseMessage("Added successful!"),HttpStatus.OK);
    }//create

    @GetMapping
    public List<Product> getAll(){
        return productService.showAllProducts();
    }//getAll

    @GetMapping("/paged")
    public ResponseEntity getAll(@RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber, @RequestParam(value = "pageSize",defaultValue = "10") int pageSize, @RequestParam(value = "sortBy", defaultValue = "id") String sortBy){
        List<Product> result = productService.showAllProducts(pageNumber,pageSize,sortBy);
        if(result.size() <= 0){
            return new ResponseEntity(new ResponseMessage("No results!"), HttpStatus.OK);
        }
        return new ResponseEntity(result,HttpStatus.OK);
    }//getAll


}//ProductsController
