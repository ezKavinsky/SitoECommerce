package progettopsw.sitoecommerce.services;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import progettopsw.sitoecommerce.entities.*;
import progettopsw.sitoecommerce.repositories.*;
import progettopsw.sitoecommerce.support.exceptions.*;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.util.Date;
import java.util.List;

@Service
public class PurchasingService {
    @Autowired
    private PurchaseRepository purchaseRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductInPurchaseRepository productInPurchaseRepository;
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private ProductInPromoPurchaseRepository productInPromoPurchaseRepository;

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public Purchase addPurchase(Purchase purchase, String id)
            throws QuantityProductUnavailableException, CreditCardNotFoundException, PurchaseAlreadyExistsException {
        if(purchaseRepository.existsById(purchase.getId())){
            throw new PurchaseAlreadyExistsException();
        }
        int ident = Integer.parseInt(id);
        Purchase result = purchaseRepository.save(purchase);
        result.setPurchaseTime(Date.from(Instant.now()));
        result.setShipped(false);
        float total = 0;
        for(ProductInPromoPurchase pipp : result.getProductsInPromoPurchase()){
            pipp.setPurchase(result);
            ProductInPromoPurchase justAdded = productInPromoPurchaseRepository.save(pipp);
            justAdded.setFinalPrice(justAdded.getProductInPromo().getDiscountPrice());
            total += justAdded.getFinalPrice();
            entityManager.refresh(justAdded);
            Product product = justAdded.getProductInPromo().getProduct();
            int newQuantity = justAdded.getQuantity() - pipp.getQuantity();
            if(newQuantity < 0){
                throw new QuantityProductUnavailableException();
            }
            product.setQuantity(newQuantity);
            product.setCart(null);
            entityManager.refresh(pipp);
        }
        entityManager.refresh(result);
        for(ProductInPurchase pip : result.getProductsInPurchase()){
            pip.setPurchase(result);
            ProductInPurchase justAdded = productInPurchaseRepository.save(pip);
            justAdded.setFinalPrice(justAdded.getProduct().getPrice()+justAdded.getProduct().getShippingPrice());
            total += justAdded.getFinalPrice();
            entityManager.refresh(justAdded); //l'entityManager ripreleva il prodotto a cui adesso è stato assegnato l'id numerico ed ha tutti i campi aggiornati
            Product product = justAdded.getProduct();
            int newQuantity = justAdded.getQuantity() - pip.getQuantity();
            if (newQuantity < 0) {
                throw new QuantityProductUnavailableException();
            }
            product.setQuantity(newQuantity);
            product.setCart(null);
            entityManager.refresh(pip);
        }
        result.setTotal(total);
        result.setBuyer(userRepository.getById(ident));
        result.setCreditCard(userRepository.getById(ident).getCreditCards().get(1));
        userRepository.getById(ident).getPurchases().add(result);
        entityManager.refresh(result);
        return result;
    }//addPurchase

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void cancelPurchase(String id) throws  PurchaseAlreadyShippedException{
        int ident = Integer.parseInt(id);
        Purchase purchase = purchaseRepository.getById(ident);
        if(!purchase.isShipped()){
            for(ProductInPurchase pip : purchase.getProductsInPurchase()){
                Product product = pip.getProduct();
                product.setQuantity(product.getQuantity()+pip.getQuantity());
                entityManager.refresh(product);
                productInPurchaseRepository.delete(pip);
            }
            for(ProductInPromoPurchase pipp : purchase.getProductsInPromoPurchase()){
                Product product = pipp.getProductInPromo().getProduct();
                entityManager.refresh(product);
                product.setQuantity(product.getQuantity()+pipp.getQuantity());
                productInPromoPurchaseRepository.delete(pipp);
            }
            purchaseRepository.delete(purchase);
        } else {
            throw new PurchaseAlreadyShippedException();
        }
    }//removePurchase

    @Transactional(readOnly = true)
    public List<Purchase> getPurchaseByUser(String id) throws UserNotFoundException {
        int ident = Integer.parseInt(id);
        if ( !userRepository.existsById(ident)){
            throw new UserNotFoundException();
        }
        User user = userRepository.getById(ident);
        return purchaseRepository.findByBuyer(user);
    }//getPurchaseByUser

    @Transactional(readOnly = true)
    public List<Purchase> getPurchaseByAdvancedSearch(@PathVariable String id, Date startDate, Date endDate, boolean shipped) throws UserNotFoundException, DateWrongRangeException{
        int ident = Integer.parseInt(id);
        if ( !userRepository.existsById(ident)){
            throw new UserNotFoundException();
        }
        if ( startDate.compareTo(endDate) >=0 ){
            throw new DateWrongRangeException();
        }
        User user = userRepository.getById(ident);
        return purchaseRepository.advancedSearch(startDate,endDate,user,shipped);
    }//getPurchaseByAdvancedSearch

    @Transactional(readOnly = true)
    public List<ProductInPurchase> getProductsByPurchase(String id){
        int ident = Integer.parseInt(id);
        Purchase purchase = purchaseRepository.getById(ident);
        return purchase.getProductsInPurchase();
    }//getProductsInPurchase

    @Transactional(readOnly = true)
    public List<ProductInPromoPurchase> getProductsInPromoByPurchase(String id){
        int ident = Integer.parseInt(id);
        Purchase purchase = purchaseRepository.getById(ident);
        return purchase.getProductsInPromoPurchase();
    }//getProductInPromoByPurchase

    @Transactional(readOnly = true)
    public Purchase getPurchase(String id){
        int ident = Integer.parseInt(id);
        return purchaseRepository.getById(ident);
    }//getPurchase

}//PurchasingService
