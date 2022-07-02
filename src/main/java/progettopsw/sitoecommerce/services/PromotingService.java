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
import progettopsw.sitoecommerce.repositories.PromoRepository;
import progettopsw.sitoecommerce.support.exceptions.*;
import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

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
    public void removePromo(String id){
        Promo result = null;
        int ident = Integer.parseInt(id);
        result = promoRepository.getById(ident);
        for(ProductInPromo pip : result.getProductsInPromo()){
            productInPromoRepository.delete(pip);
        }
        promoRepository.delete(result);
    }//removePromo

    @Transactional(readOnly = true)
    public List<ProductInPromo> getProductsInPromo(String id) throws PromoNotFoundException{
        int ident = Integer.parseInt(id);
        if(promoRepository.existsById(ident)){
            return promoRepository.getById(ident).getProductsInPromo();
        } else {
            throw new PromoNotFoundException();
        }
    }//getProductsInPromo

    @Transactional(readOnly = true)
    public List<ProductInPromo> getProductsInPromoPaged(int pageNumber, int pageSize, String sortBy, String id){
        int ident = Integer.parseInt(id);
        Promo promo = promoRepository.getById(ident);
        Pageable paging = PageRequest.of(pageNumber,pageSize, Sort.by(sortBy));
        Page<ProductInPromo> pagedResult = productInPromoRepository.advancedPagedSearch(promo, null, paging);
        if(pagedResult.hasContent()){
            return pagedResult.getContent();
        }
        else {
            return new ArrayList<>();
        }
    }//getProductsInPromo

    @Transactional(readOnly = true)
    public String getPromoName(String  id){
        int ident = Integer.parseInt(id);
        return promoRepository.getById(ident).getName();
    }//getPromoName

}//PromoService
