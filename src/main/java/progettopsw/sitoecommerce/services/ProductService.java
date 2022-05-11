package progettopsw.sitoecommerce.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import progettopsw.sitoecommerce.entities.Product;
import progettopsw.sitoecommerce.repositories.ProductRepository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Transactional(readOnly = false)
    public void addProduct(Product product) throws BarCodeAlreadyExistsException{
        if(product.getBarCode() != null && productRepository.existsByBarCode(product.getBarCode())){
            throw new BarCodeAlreadyExistsException();
        }
        productRepository.save(product);
    }//addProduct

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
    public List<Product> showProductsByName(String name){ return productRepository.findByNameContaining(name); }

    @Transactional(readOnly = true)
    public List<Product> showProductsByBarCode(String barCode) { return productRepository.findByBarCode(barCode); }

}//ProductService
