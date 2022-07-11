package progettopsw.sitoecommerce.services;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import progettopsw.sitoecommerce.entities.Product;
import progettopsw.sitoecommerce.support.exceptions.BarCodeAlreadyExistException;
import progettopsw.sitoecommerce.repositories.ProductRepository;
import progettopsw.sitoecommerce.support.exceptions.ProductNotFoundException;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public Product addProduct(Product product) throws BarCodeAlreadyExistException {
        if(productRepository.existsByBarCode(product.getBarCode())){
            throw new BarCodeAlreadyExistException();
        }
        product.setVersion(1);
        Product result = productRepository.save(product);
        return result;
    }//addProduct

    @Transactional(readOnly = false,propagation = Propagation.REQUIRED)
    public void removeProduct(String id){
        int ident = Integer.parseInt(id);
        Product product = productRepository.getById(ident);
        productRepository.delete(product);
    }//removeProduct

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public Product updateName(String name, String id) throws ProductNotFoundException{
        int ident = Integer.parseInt(id);
        Product result;
        if(productRepository.existsById(ident)){
            result = productRepository.getById(ident);
            result.setName(name);
        } else {
            throw new ProductNotFoundException();
        }
        return result;
    }//updateName

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public Product updateBrand(String brand, String id) throws ProductNotFoundException{
        int ident = Integer.parseInt(id);
        Product result;
        if(productRepository.existsById(ident)){
            result = productRepository.getById(ident);
            result.setBrand(brand);
        } else {
            throw new ProductNotFoundException();
        }
        return result;
    }//updateBrand

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public Product updateBarCode(String barCode, String id) throws ProductNotFoundException{
        int ident = Integer.parseInt(id);
        Product result;
        if(productRepository.existsById(ident)){
            result = productRepository.getById(ident);
            result.setBarCode(barCode);
        } else {
            throw new ProductNotFoundException();
        }
        return result;
    }//updateBarCode

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public Product updateDescription(String description, String id) throws ProductNotFoundException{
        int ident = Integer.parseInt(id);
        Product result;
        if(productRepository.existsById(ident)){
            result = productRepository.getById(ident);
            result.setDescription(description);
        } else {
            throw new ProductNotFoundException();
        }
        return result;
    }//updateDescription

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public Product updatePrice(float price, String id) throws ProductNotFoundException{
        int ident = Integer.parseInt(id);
        Product result;
        if(productRepository.existsById(ident)){
           result = productRepository.getById(ident);
           result.setPrice(price);
        } else {
            throw new ProductNotFoundException();
        }
        return result;
    }//updatePrice

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public Product updateQuantity(int quantity, String id) throws ProductNotFoundException{
        int ident = Integer.parseInt(id);
        Product result;
        if(productRepository.existsById(ident)){
            result = productRepository.getById(ident);
            result.setQuantity(quantity);
        } else {
            throw new ProductNotFoundException();
        }
        return result;
    }//updateQuantity

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public Product updateProductionYear(int year, String id) throws ProductNotFoundException{
        int ident = Integer.parseInt(id);
        Product result;
        if(productRepository.existsById(ident)){
            result = productRepository.getById(ident);
            result.setProductionYear(year);
        } else {
            throw new ProductNotFoundException();
        }
        return result;
    }//updateProductionYear

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public Product updateFreeShipping(boolean cond, String id) throws ProductNotFoundException{
        int ident = Integer.parseInt(id);
        Product result;
        if(productRepository.existsById(ident)){
            result = productRepository.getById(ident);
            result.setFreeShipping(cond);
        } else {
            throw new ProductNotFoundException();
        }
        return result;
    }//removeFreeShipping

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public Product updateShippingPrice(float price, String id) throws ProductNotFoundException{
        int ident = Integer.parseInt(id);
        Product result;
        if(productRepository.existsById(ident)){
            result = productRepository.getById(ident);
            if(result.isFreeShipping()){
                result.setShippingPrice(0);
            } else {
                result.setShippingPrice(price);
            }
        } else {
            throw new ProductNotFoundException();
        }
        return result;
    }//updateShippingPrice

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public Product updateScore(float score, String id) throws ProductNotFoundException{
        int ident = Integer.parseInt(id);
        Product result;
        if(productRepository.existsById(ident)){
           result = productRepository.getById(ident);
           result.setScore(score);
        } else {
            throw new ProductNotFoundException();
        }
        return result;
    }//updateScore

    @Transactional(readOnly = true)
    public List<Product> showAllProducts(){
        return productRepository.findAll();
    }//showAllProducts

    @Transactional(readOnly = true)
    public List<Product> showAllProducts(int pageNumber, int pageSize, String sortBy){
        Pageable paging = PageRequest.of(pageNumber,pageSize, Sort.by(sortBy));
        Page<Product> pagedResult = productRepository.findAll(paging);
        if(pagedResult.hasContent()){
            return pagedResult.getContent();
        }
        else {
            return new ArrayList<>();
        }
    }//showAllProducts

    @Transactional(readOnly = true)
    public List<Product> showProductsByAdvancedSearch(String name, String brand, int lowPrice, int highPrice, int lowYear, int highYear,
                                                      boolean shipping, int lowScore, int highScore){
        return productRepository.advancedSearch(name, brand, lowPrice, highPrice, lowYear, highYear, shipping, lowScore, highScore);
    }//showAllProducts

    @Transactional(readOnly = true)
    public List<Product> showProductsByAdvancedPagedSearch(int pageNumber, int pageSize, String sortBy, String name, String brand, int lowPrice,
                                                      int highPrice, int lowYear, int highYear, boolean freeShipping, int lowScore, int highScore){
        Pageable paging = PageRequest.of(pageNumber,pageSize, Sort.by(sortBy));
        Page<Product> pagedResult = productRepository.advancedPagedSearch(name, brand, lowPrice, highPrice, lowYear, highYear, freeShipping, lowScore,
                highScore, paging);
        if(pagedResult.hasContent()){
            return pagedResult.getContent();
        }
        else {
            return new ArrayList<>();
        }
    }//showAllProducts

    @Transactional(readOnly = true)
    public Product showProductByBarCode(String barCode) {
        return productRepository.findByBarCode(barCode);
    }

    @Transactional(readOnly = true)
    public Product getProduct(String id){
        int ident = Integer.parseInt(id);
        return productRepository.getById(ident);
    }//getProduct

    @Transactional(readOnly = true)
    public List<Product> showProductsByName(String name) {
        return productRepository.findByNameContaining(name);
    }

}//ProductService