package progettopsw.sitoecommerce.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import progettopsw.sitoecommerce.entities.Product;
import progettopsw.sitoecommerce.entities.ProductInPurchase;
import progettopsw.sitoecommerce.entities.Purchase;
import progettopsw.sitoecommerce.entities.User;
import progettopsw.sitoecommerce.support.exceptions.DateWrongRangeException;
import progettopsw.sitoecommerce.support.exceptions.QuantityProductUnavailableException;
import progettopsw.sitoecommerce.support.exceptions.UserNotFoundException;
import progettopsw.sitoecommerce.repositories.ProductInPurchaseRepository;
import progettopsw.sitoecommerce.repositories.PurchaseRepository;
import progettopsw.sitoecommerce.repositories.UserRepository;
import javax.persistence.EntityManager;
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

    @Transactional(readOnly = false)
    public Purchase addPurchase(Purchase purchase) throws QuantityProductUnavailableException {
        Purchase result = purchaseRepository.save(purchase);
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

    @Transactional(readOnly = true)
    public List<Purchase> getPurchaseByUser(User user) throws UserNotFoundException {
        if ( !userRepository.existsById(user.getId())){
            throw new UserNotFoundException();
        }
        return purchaseRepository.findByBuyer(user);
    }//getPurchaseByUser

    @Transactional(readOnly = true)
    public List<Purchase> getPurchaseByUserInPeriod(User user, Date startDate, Date endDate) throws UserNotFoundException, DateWrongRangeException{
        if ( !userRepository.existsById(user.getId())){
            throw new UserNotFoundException();
        }
        if ( startDate.compareTo(endDate) >=0 ){
            throw new DateWrongRangeException();
        }
        return purchaseRepository.findByBuyerInPeriod(startDate,endDate,user);


    }
}//PurchasingService
