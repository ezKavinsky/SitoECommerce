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
    public void addProduct(Product product) throws BarCodeAlreadyExistException {
        if(productRepository.existsByBarCode(product.getBarCode())){
            throw new BarCodeAlreadyExistException();
        }
        productRepository.save(product);
    }//addProduct

    @Transactional(readOnly = false,propagation = Propagation.REQUIRED)
    public void removeProduct(Product product) throws ProductNotFoundException{
        if(!productRepository.existsByBarCode(product.getBarCode()))
            throw new ProductNotFoundException();
        productRepository.delete(product);
    }//removeProduct

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void updateName(String name, Product product) throws ProductNotFoundException{
        if(productRepository.existsById(product.getId())){
            productRepository.getById(product.getId()).setName(name);
        } else {
            throw new ProductNotFoundException();
        }
    }//updateName

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void updateBrand(String brand, Product product) throws ProductNotFoundException{
        if(productRepository.existsById(product.getId())){
            productRepository.getById(product.getId()).setBrand(brand);
        } else {
            throw new ProductNotFoundException();
        }
    }//updateBrand

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void updateBarCode(String barCode, Product product) throws ProductNotFoundException{
        if(productRepository.existsById(product.getId())){
            productRepository.getById(product.getId()).setBarCode(barCode);
        } else {
            throw new ProductNotFoundException();
        }
    }//updateBarCode

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void updateDescription(String description, Product product) throws ProductNotFoundException{
        if(productRepository.existsById(product.getId())){
            productRepository.getById(product.getId()).setDescription(description);
        } else {
            throw new ProductNotFoundException();
        }
    }//updateDescription

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void updatePrice(float price, Product product) throws ProductNotFoundException{
        if(productRepository.existsById(product.getId())){
            productRepository.getById(product.getId()).setPrice(price);
        } else {
            throw new ProductNotFoundException();
        }
    }//updatePrice

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void updateQuantity(int quantity, Product product) throws ProductNotFoundException{
        if(productRepository.existsById(product.getId())){
            productRepository.getById(product.getId()).setQuantity(quantity);
        } else {
            throw new ProductNotFoundException();
        }
    }//updateQuantity

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void updateProductionYear(int year, Product product) throws ProductNotFoundException{
        if(productRepository.existsById(product.getId())){
            productRepository.getById(product.getId()).setProductionYear(year);
        } else {
            throw new ProductNotFoundException();
        }
    }//updateProductionYear

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void updateFreeShipping(boolean cond, Product product) throws ProductNotFoundException{
        if(productRepository.existsById(product.getId())){
            productRepository.getById(product.getId()).setFreeShipping(cond);
        } else {
            throw new ProductNotFoundException();
        }
    }//removeFreeShipping

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void updateShippingPrice(float price, Product product) throws ProductNotFoundException{
        if(productRepository.existsById(product.getId())){
            if(productRepository.getById(product.getId()).isFreeShipping()){
                productRepository.getById(product.getId()).setShippingPrice(0);
            } else {
                productRepository.getById(product.getId()).setShippingPrice(price);
            }
        } else {
            throw new ProductNotFoundException();
        }
    }//updateShippingPrice

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void updateScore(float score, Product product) throws ProductNotFoundException{
        if(productRepository.existsById(product.getId())){
            productRepository.getById(product.getId()).setScore(score);
        } else {
            throw new ProductNotFoundException();
        }
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
    public List<Product> showProductsByAdvancedSearch(String name, String brand, int quantity, int lowPrice, int highPrice, int lowYear, int highYear,
                                                      boolean shipping, int lowScore, int highScore){
        return productRepository.advancedSearch(name, brand, quantity, lowPrice, highPrice, lowYear, highYear, shipping, lowScore, highScore);
    }//showAllProducts

    @Transactional(readOnly = true)
    public List<Product> showProductsByAdvancedPagedSearch(int pageNumber, int pageSize, String sortBy, String name, String brand, int quantity, int lowPrice,
                                                      int highPrice, int lowYear, int highYear, boolean shipping, int lowScore, int highScore){
        Pageable paging = PageRequest.of(pageNumber,pageSize, Sort.by(sortBy));
        Page<Product> pagedResult = productRepository.advancedPagedSearch(name, brand, quantity, lowPrice, highPrice, lowYear, highYear, shipping, lowScore,
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

}//ProductService