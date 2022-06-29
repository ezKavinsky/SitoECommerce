package progettopsw.sitoecommerce.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import progettopsw.sitoecommerce.entities.Product;
import progettopsw.sitoecommerce.entities.ProductInPromo;
import progettopsw.sitoecommerce.entities.Promo;
import progettopsw.sitoecommerce.repositories.ProductInPromoRepository;
import progettopsw.sitoecommerce.repositories.ProductRepository;
import progettopsw.sitoecommerce.repositories.PromoRepository;
import progettopsw.sitoecommerce.support.exceptions.*;

import java.util.List;

@Service
public class PromotingService {
    @Autowired
    private PromoRepository promoRepository;
    @Autowired
    private ProductInPromoRepository productInPromoRepository;
    @Autowired
    private ProductRepository productRepository;

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public Promo addPromo(Promo promo) throws PromoAlreadyExistsException{
        Promo result = null;
        if(!promoRepository.existsById(promo.getId())){
            result = promoRepository.save(promo);
            for(ProductInPromo pip : promo.getProductsInPromo()){
                pip.setPromo(promo);
                pip.setDiscountPrice(pip.getProduct().getPrice()-(pip.getProduct().getPrice()* promo.getDiscount())/100);
                ProductInPromo justAdded = productInPromoRepository.save(pip);
            }
        } else {
            throw new PromoAlreadyExistsException();
        }
        return result;
    }//addPromo

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public Promo removePromo(Promo promo) throws PromoNotFoundException {
        Promo result = null;
        if(!promoRepository.existsById(promo.getId())){
            throw new PromoNotFoundException();
        } else {
            result = promoRepository.getById(promo.getId());
            for(ProductInPromo pip : promo.getProductsInPromo()){
                productInPromoRepository.delete(pip);
            }
            promoRepository.delete(promo);
        }
        return result;
    }//removePromo

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public ProductInPromo addProductInPromo(Promo promo, Product product) throws ProductAlreadyInThisPromoException,ProductNotFoundException,
            PromoNotFoundException {
        ProductInPromo result = null;
        if(promoRepository.existsById(promo.getId())){
            if(productRepository.existsById(product.getId())){
                if(!productInPromoRepository.existsByProductAndPromo(product, promo)) {
                    ProductInPromo pip = new ProductInPromo();
                    pip.setProduct(product);
                    pip.setPromo(promo);
                    pip.setDiscountPrice(product.getPrice() - (product.getPrice() * promo.getDiscount()) / 100);
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
    public ProductInPromo removeProductFromPromo(Promo promo, ProductInPromo productInPromo) throws ProductNotInThisPromoException,
            PromoNotFoundException{
        ProductInPromo result = null;
        if(promoRepository.existsById(promo.getId())) {
            if (productInPromoRepository.existsByProductAndPromo(productInPromo.getProduct(), promo)) {
                result = productInPromo;
                promo.getProductsInPromo().remove(productInPromo);
                productInPromoRepository.delete(productInPromo);
            } else {
                throw new ProductNotInThisPromoException();
            }
        } else {
            throw new PromoNotFoundException();
        }
        return result;
    }//removeProductFromPromo

    @Transactional(readOnly = true)
    public

}//PromoService
