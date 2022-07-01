package progettopsw.sitoecommerce.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import progettopsw.sitoecommerce.entities.ProductInPromo;
import progettopsw.sitoecommerce.entities.Promo;
import progettopsw.sitoecommerce.repositories.ProductInPromoRepository;
import progettopsw.sitoecommerce.repositories.PromoRepository;
import progettopsw.sitoecommerce.support.exceptions.*;
import javax.persistence.EntityManager;

@Service
public class PromotingService {
    @Autowired
    private PromoRepository promoRepository;
    @Autowired
    private ProductInPromoRepository productInPromoRepository;
    @Autowired
    private EntityManager entityManager;

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public Promo addPromo(Promo promo) throws PromoAlreadyExistsException{
        Promo result = null;
        if(!promoRepository.existsById(promo.getId())){
            result = promoRepository.save(promo);
            for(ProductInPromo pip : result.getProductsInPromo()){
                pip.setPromo(result);
                pip.setDiscountPrice(pip.getProduct().getPrice()-(pip.getProduct().getPrice()* result.getDiscount())/100);
                productInPromoRepository.save(pip);
            }
            entityManager.refresh(result);
        } else {
            throw new PromoAlreadyExistsException();
        }
        return result;
    }//addPromo

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public Promo removePromo(String id) throws PromoNotFoundException {
        Promo result = null;
        int ident = Integer.parseInt(id);
        if(!promoRepository.existsById(ident)){
            throw new PromoNotFoundException();
        } else {
            result = promoRepository.getById(ident);
            for(ProductInPromo pip : result.getProductsInPromo()){
                productInPromoRepository.delete(pip);
            }
            promoRepository.delete(result);
        }
        return result;
    }//removePromo

    @Transactional(readOnly = true)
    public Promo getPromoByProduct(ProductInPromo productInPromo){
        return promoRepository.findByProductInPromo(productInPromo);
    }  //getPromoByProduct

}//PromoService
