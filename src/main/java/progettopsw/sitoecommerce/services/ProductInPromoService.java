package progettopsw.sitoecommerce.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import progettopsw.sitoecommerce.entities.Product;
import progettopsw.sitoecommerce.entities.ProductInPromo;
import progettopsw.sitoecommerce.entities.Promo;
import progettopsw.sitoecommerce.repositories.ProductInPromoRepository;
import progettopsw.sitoecommerce.repositories.ProductRepository;
import progettopsw.sitoecommerce.repositories.PromoRepository;
import progettopsw.sitoecommerce.support.exceptions.ProductAlreadyInThisPromoException;
import progettopsw.sitoecommerce.support.exceptions.ProductNotFoundException;
import progettopsw.sitoecommerce.support.exceptions.ProductNotInThisPromoException;
import progettopsw.sitoecommerce.support.exceptions.PromoNotFoundException;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductInPromoService {
    @Autowired
    private ProductInPromoRepository productInPromoRepository;
    @Autowired
    private PromoRepository promoRepository;
    @Autowired
    private ProductRepository productRepository;

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public ProductInPromo addProductInPromo(String id1, String id2) throws ProductAlreadyInThisPromoException, ProductNotFoundException,
            PromoNotFoundException {
        int ident1 = Integer.parseInt(id1);
        int ident2 = Integer.parseInt(id2);
        ProductInPromo result = null;
        if(promoRepository.existsById(ident1)){
            if(productRepository.existsById(ident2)){
                Product product = productRepository.getById(ident2);
                Promo promo = promoRepository.getById(ident1);
                if(!productInPromoRepository.existsByProductAndPromo(product, promo)) {
                    ProductInPromo pip = new ProductInPromo();
                    pip.setProduct(product);
                    pip.setPromo(promo);
                    pip.setDiscountPrice(product.getPrice() - (product.getPrice() * promo.getDiscount()) / 100);
                    productInPromoRepository.save(pip);
                    result = pip;
                } else {
                    throw new ProductAlreadyInThisPromoException();
                }
            } else {
                throw new ProductNotFoundException();
            }
        } else {
            throw new PromoNotFoundException();
        }
        return result;
    }//addProductInPromo

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void removeProductFromPromo(String id1, String id2) throws ProductNotInThisPromoException,
            PromoNotFoundException{
        int ident1 = Integer.parseInt(id1);
        int ident2 = Integer.parseInt(id2);
        if(promoRepository.existsById(ident2)) {
            Promo promo = promoRepository.getById(ident2);
            ProductInPromo productInPromo = productInPromoRepository.getById(ident1);
            if (productInPromoRepository.existsByProductAndPromo(productInPromo.getProduct(), promo)) {
                promo.getProductsInPromo().remove(productInPromo);
                productInPromoRepository.delete(productInPromo);
            } else {
                throw new ProductNotInThisPromoException();
            }
        } else {
            throw new PromoNotFoundException();
        }
    }//removeProductFromPromo

    @Transactional(readOnly = true)
    public List<ProductInPromo> showAllProductsInPromo(){
        return productInPromoRepository.findAll();
    }//showAllProducts

    @Transactional(readOnly = true)
    public List<ProductInPromo> showAllProductsInPromo(int pageNumber, int pageSize, String sortBy){
        Pageable paging = PageRequest.of(pageNumber,pageSize, Sort.by(sortBy));
        Page<ProductInPromo> pagedResult = productInPromoRepository.findAll(paging);
        if(pagedResult.hasContent()){
            return pagedResult.getContent();
        }
        else {
            return new ArrayList<>();
        }
    }//showAllProducts

    @Transactional(readOnly = true)
    public List<ProductInPromo> showProductsInPromoByAdvancedSearch(Promo promo, Product product) {
        return productInPromoRepository.advancedSearch(promo, product);
    }//showAllProducts

    @Transactional(readOnly = true)
    public List<ProductInPromo> showProductsInPromoByAdvancedPagedSearch(int pageNumber, int pageSize, String sortBy, Promo promo, Product product){
        Pageable paging = PageRequest.of(pageNumber,pageSize, Sort.by(sortBy));
        Page<ProductInPromo> pagedResult = productInPromoRepository.advancedPagedSearch(promo, product, paging);
        if(pagedResult.hasContent()){
            return pagedResult.getContent();
        }
        else {
            return new ArrayList<>();
        }
    }//showAllProducts

    @Transactional(readOnly = true)
    public List<ProductInPromo> getProductsByPromo(Promo promo){
        return productInPromoRepository.advancedSearch(promo, null);
    }//getProductsByPromo

}//ProductInPromoRepository
