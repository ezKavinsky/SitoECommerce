package progettopsw.sitoecommerce.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import progettopsw.sitoecommerce.entities.*;
import progettopsw.sitoecommerce.repositories.*;
import progettopsw.sitoecommerce.support.exceptions.*;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class PurchasingService {
    @Autowired
    private PurchaseRepository purchaseRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CreditCardRepository creditCardRepository;
    @Autowired
    private ProductInPurchaseRepository productInPurchaseRepository;
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private ProductInPromoPurchaseRepository productInPromoPurchaseRepository;

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public Purchase addPurchase(Purchase purchase) throws QuantityProductUnavailableException,
            CreditCardNotFoundException, CreditCardExpiredException, PurchaseAlreadyExistsException {
        if(isExpired(purchase.getCreditCard())){
            throw new CreditCardExpiredException();
        }
        if(purchaseRepository.existsById(purchase.getId())){
            throw new PurchaseAlreadyExistsException();
        }
        Purchase result = purchaseRepository.save(purchase);
        for(ProductInPromoPurchase pipp : result.getProductsInPromoPurchase()){
            pipp.setPurchase(result);
            ProductInPromoPurchase justAdded = productInPromoPurchaseRepository.save(pipp);
            entityManager.refresh(justAdded);
            Product product = justAdded.getProductInPromo().getProduct();
            int newQuantity = justAdded.getQuantity() - pipp.getQuantity();
            if(newQuantity < 0){
                throw new QuantityProductUnavailableException();
            }
            product.setQuantity(newQuantity);
            entityManager.refresh(pipp);
        }
        entityManager.refresh(result);
        for(ProductInPurchase pip : result.getProductsInPurchase()){
            pip.setPurchase(result);
            ProductInPurchase justAdded = productInPurchaseRepository.save(pip);
            entityManager.refresh(justAdded); //l'entityManager ripreleva il prodotto a cui adesso è stato assegnato l'id numerico ed ha tutti i campi aggiornati
            Product product = justAdded.getProduct();
            int newQuantity = justAdded.getQuantity() - pip.getQuantity();
            if (newQuantity < 0) {
                throw new QuantityProductUnavailableException();
            }
            product.setQuantity(newQuantity);
            entityManager.refresh(pip);
        }
        entityManager.refresh(result);
        return result;
    }//addPurchase

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void cancelPurchase(String id) throws  PurchaseAlreadyShippedException, PurchaseNotFoundException{
        int ident = Integer.parseInt(id);
        if(purchaseRepository.existsById(ident)){
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
        } else {
            throw new PurchaseNotFoundException();
        }
    }//removePurchase

    @Transactional(readOnly = true)
    public List<Purchase> getPurchaseByUser(User user) throws UserNotFoundException {
        if ( !userRepository.existsById(user.getId())){
            throw new UserNotFoundException();
        }
        return purchaseRepository.findByBuyer(user);
    }//getPurchaseByUser

    @Transactional(readOnly = true)
    public List<Purchase> getPurchaseByAdvancedSearch(User user, Date startDate, Date endDate, boolean shipped) throws UserNotFoundException, DateWrongRangeException{
        if ( !userRepository.existsById(user.getId())){
            throw new UserNotFoundException();
        }
        if ( startDate.compareTo(endDate) >=0 ){
            throw new DateWrongRangeException();
        }
        return purchaseRepository.advancedSearch(startDate,endDate,user,shipped);
    }//getPurchaseByAdvancedSearch

    @Transactional(readOnly = true)
    public boolean isExpired(CreditCard creditCard) throws CreditCardNotFoundException {
        if(!creditCardRepository.existsByNumber(creditCard.getNumber())){
            throw new CreditCardNotFoundException();
        } else {
            Calendar now = Calendar.getInstance();
            return (now.YEAR - creditCard.getExpirationYear() >= 0) && (now.MONTH - creditCard.getExpirationMonth() >= 0);
        }
    }//checkExpirationDate

    @Transactional(readOnly = true)
    public List<ProductInPurchase> getProductsByPurchase(String id){
        int ident = Integer.parseInt(id);
        Purchase purchase = purchaseRepository.getById(ident);
        return productInPurchaseRepository.advancedSearch(purchase,null);
    }//getProductsInPurchase

    @Transactional(readOnly = true)
    public List<ProductInPromoPurchase> getProductsInPromoByPurchase(String id){
        int ident = Integer.parseInt(id);
        Purchase purchase = purchaseRepository.getById(ident);
        return productInPromoPurchaseRepository.advancedSearch(purchase,null);
    }//getProductInPromoByPurchase
    
}//PurchasingService
