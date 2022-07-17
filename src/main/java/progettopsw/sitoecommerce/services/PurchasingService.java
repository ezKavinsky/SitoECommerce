package progettopsw.sitoecommerce.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import progettopsw.sitoecommerce.entities.*;
import progettopsw.sitoecommerce.entities.ProductInPromoInCart;
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
    @Autowired
    private CartRepository cartRepository;

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public Purchase addPurchase(String id)
            throws QuantityProductUnavailableException, CreditCardNotFoundException {
        int ident = Integer.parseInt(id);
        Cart cart = cartRepository.findByBuyer(ident);
        Purchase p = new Purchase();
        p.setPurchaseTime(Date.from(Instant.now()));
        p.setShipped(false);
        Purchase result = purchaseRepository.save(p);
        float total = 0;
        for(ProductInPromoInCart pipic : cart.getProductsInPromo()){
            ProductInPromoPurchase pipp = new ProductInPromoPurchase();
            pipp.setProductInPromo(pipic.getProductInPromo());
            pipp.setPurchase(result);
            pipp.setQuantity(pipic.getQuantity());
            ProductInPromoPurchase justAdded = productInPromoPurchaseRepository.save(pipp);
            justAdded.setFinalPrice(justAdded.getProductInPromo().getDiscountPrice()*justAdded.getQuantity());
            total += justAdded.getFinalPrice();
            entityManager.merge(justAdded);
            Product product = justAdded.getProductInPromo().getProduct();
            int newQuantity = product.getQuantity() - pipp.getQuantity();
            if(newQuantity < 0){
                throw new QuantityProductUnavailableException();
            }
            result.getProductsInPromoPurchase().add(pipp);
            product.setQuantity(newQuantity);
            entityManager.merge(pipp);
        }
        cart.getProductsInPromo().clear();
        entityManager.merge(result);
        for(ProductInCart pic : cart.getProducts()){
            ProductInPurchase pip = new ProductInPurchase();
            pip.setProduct(pic.getProduct());
            pip.setPurchase(result);
            pip.setQuantity(pic.getQuantity());
            ProductInPurchase justAdded = productInPurchaseRepository.save(pip);
            justAdded.setFinalPrice((justAdded.getProduct().getPrice()*justAdded.getQuantity())+justAdded.getProduct().getShippingPrice());
            total += justAdded.getFinalPrice();
            entityManager.merge(justAdded); //l'entityManager ripreleva il prodotto a cui adesso è stato assegnato l'id numerico ed ha tutti i campi aggiornati
            Product product = justAdded.getProduct();
            int newQuantity = product.getQuantity() - pip.getQuantity();
            if (newQuantity < 0) {
                throw new QuantityProductUnavailableException();
            }
            product.setQuantity(newQuantity);
            result.getProductsInPurchase().add(pip);
            entityManager.merge(pip);
        }
        cart.getProducts().clear();
        result.setTotal(total);
        result.setBuyer(cart.getBuyer());
        result.setCreditCard(userRepository.getById(ident).getCreditCards().get(0));
        userRepository.getById(ident).getPurchases().add(result);
        entityManager.merge(result);
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
                entityManager.merge(product);
                productInPurchaseRepository.delete(pip);
            }
            for(ProductInPromoPurchase pipp : purchase.getProductsInPromoPurchase()){
                Product product = pipp.getProductInPromo().getProduct();
                entityManager.merge(product);
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

    @Transactional(readOnly = false)
    public void setShipped(String id){
        int ident = Integer.parseInt(id);
        purchaseRepository.getById(ident).setShipped(true);
    }//setShipped

}//PurchasingService
