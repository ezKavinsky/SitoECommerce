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
    public ProductInPromo addProductInPromo(Product product, String id) throws ProductAlreadyInThisPromoException, ProductNotFoundException,
            PromoNotFoundException {
        ProductInPromo result = null;
        int ident = Integer.parseInt(id);
        if(promoRepository.existsById(ident)){
            Promo promo = promoRepository.getById(ident);
            if(productRepository.existsById(product.getId())){
                if(!productInPromoRepository.existsByProductAndPromo(product, promo)) {
                    ProductInPromo pip = new ProductInPromo();
                    pip.setProduct(product);
                    pip.setPromo(promo);
                    promo.getProductsInPromo().add(pip);
                    pip.setDiscountPrice(product.getPrice() - (product.getPrice() * promo.getDiscount()) / 100);
                    result = productInPromoRepository.save(pip);
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
    public void removeProductFromPromo(String id){
        int ident = Integer.parseInt(id);
        ProductInPromo productInPromo = productInPromoRepository.getById(ident);
        Promo promo = productInPromo.getPromo();
        promo.getProductsInPromo().remove(productInPromo);
        productInPromoRepository.delete(productInPromo);
    }//removeProductFromPromo

    @Transactional(readOnly = true)
    public List<ProductInPromo> showAllProductsInPromo(){
        return productInPromoRepository.findAll();
    }//showAllProducts

    @Transactional(readOnly = true)
    public List<ProductInPromo> showProductsInPromoPaged(int pageNumber, int pageSize, String sortBy,String id) throws PromoNotFoundException{
        Pageable paging = PageRequest.of(pageNumber,pageSize, Sort.by(sortBy));
        int ident = Integer.parseInt(id);
        if(promoRepository.existsById(ident)) {
            Promo promo = promoRepository.getById(ident);
            Page<ProductInPromo> pagedResult = productInPromoRepository.advancedPagedSearch(promo,paging);
            if (pagedResult.hasContent()) {
                return pagedResult.getContent();
            } else {
                return new ArrayList<>();
            }
        } else {
            throw new PromoNotFoundException();
        }
    }//showAllProducts

    @Transactional(readOnly = true)
    public List<ProductInPromo> showProductsInPromo(String id) {
        int ident = Integer.parseInt(id);
        return promoRepository.getById(ident).getProductsInPromo();
    }//showAllProducts


    @Transactional(readOnly = true)
    public ProductInPromo getProductInPromo(String id){
        int ident = Integer.parseInt(id);
        return productInPromoRepository.getById(ident);
    }//getProductInPromo

}//ProductInPromoRepository
