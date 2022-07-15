package progettopsw.sitoecommerce.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import progettopsw.sitoecommerce.entities.*;
import progettopsw.sitoecommerce.repositories.*;
import progettopsw.sitoecommerce.support.exceptions.ProductInCartNotFoundException;
import progettopsw.sitoecommerce.support.exceptions.ProductInPromoInCartNotFoundException;
import progettopsw.sitoecommerce.support.exceptions.ProductInPromoNotFoundException;
import progettopsw.sitoecommerce.support.exceptions.ProductNotFoundException;

import java.util.List;

@Service
public class CartService {
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductInPromoRepository productInPromoRepository;
    @Autowired
    private ProductInCartRepository productInCartRepository;
    @Autowired
    private ProductInPromoInCartRepository productInPromoInCartRepository;

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public Cart create(int id) {
        Cart cart = new Cart();
        cart.setBuyer(userRepository.getById(id));
        userRepository.getById(id).setCart(cart);
        return cartRepository.save(cart);
    }//create

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public Cart clear(String id){
        int ident = Integer.parseInt(id);
        Cart result = cartRepository.findByBuyer(ident);
        if(result.getProducts().size() > 0) {
            for (ProductInCart pic : result.getProducts()) {
                productRepository.getById(pic.getProduct().getId()).getProductsInCarts().remove(pic);
                productInCartRepository.delete(pic);
            }
            cartRepository.findByBuyer(ident).getProducts().clear();
        }
        if(result.getProductsInPromo().size() > 0) {
            for (ProductInPromoInCart pipic : result.getProductsInPromo()) {
                productInPromoRepository.getById(pipic.getProductInPromo().getId()).getProductsInPromoInCarts().remove(pipic);
                productInPromoInCartRepository.delete(pipic);
            }
            cartRepository.findByBuyer(ident).getProductsInPromo().clear();
        }
        return result;
    }//clear

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void delete(int id){
        cartRepository.delete(cartRepository.findByBuyer(id));
    }//delete

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public Cart addProduct(String id, int idC, int quantity) throws ProductNotFoundException{
        int ident = Integer.parseInt(id);
        if(productInCartRepository.existsByProduct(ident)){
            ProductInCart productInCart = productInCartRepository.getById(ident);
            productInCart.setQuantity(productInCart.getQuantity() + quantity);
            return cartRepository.getById(idC);
        }
        else if(productRepository.existsById(ident)){
            Product product = productRepository.getById(ident);
            ProductInCart pic = new ProductInCart();
            pic.setCart(cartRepository.getById(idC));
            pic.setQuantity(quantity);
            pic.setProduct(product);
            ProductInCart justAdded = productInCartRepository.save(pic);
            Cart result = cartRepository.getById(idC);
            result.getProducts().add(justAdded);
            productRepository.getById(product.getId()).getProductsInCarts().add(justAdded);
            return result;
        } else {
            throw new ProductNotFoundException();
        }
    }//addProduct

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public Cart addProductInPromo(String id, int idC, int quantity) throws ProductInPromoNotFoundException {
        int ident = Integer.parseInt(id);
        if(productInPromoInCartRepository.existsByProductInPromo(ident)){
            ProductInPromoInCart productInPromoInCart = productInPromoInCartRepository.getById(ident);
            productInPromoInCart.setQuantity(productInPromoInCart.getQuantity() + quantity);
            return cartRepository.getById(idC);
        }
        if(productInPromoRepository.existsById(ident)){
            ProductInPromo productInPromo = productInPromoRepository.getById(ident);
            ProductInPromoInCart pipic = new ProductInPromoInCart();
            pipic.setCart(cartRepository.getById(idC));
            pipic.setQuantity(quantity);
            pipic.setProductInPromo(productInPromo);
            ProductInPromoInCart justAdded = productInPromoInCartRepository.save(pipic);
            Cart result = cartRepository.getById(idC);
            result.getProductsInPromo().add(justAdded);
            productInPromoRepository.getById(productInPromo.getId()).getProductsInPromoInCarts().add(justAdded);
            return result;
        } else {
            throw new ProductInPromoNotFoundException();
        }
    }//addProductInPromo

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void removeProduct(String id, ProductInCart productInCart){
        int ident = Integer.parseInt(id);
        cartRepository.findByBuyer(ident).getProducts().remove(productInCart);
        productRepository.getById(productInCart.getProduct().getId()).getProductsInCarts().remove(productInCart);
        productInCartRepository.delete(productInCart);
    }//removeProduct

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void removeProductInPromo(String id, ProductInPromoInCart productInPromoInCart){
        int ident = Integer.parseInt(id);
        cartRepository.findByBuyer(ident).getProductsInPromo().remove(productInPromoInCart);
        productInPromoRepository.getById(productInPromoInCart.getProductInPromo().getId()).getProductsInPromoInCarts().remove(productInPromoInCart);
        productInPromoInCartRepository.delete(productInPromoInCart);
    }//removeProduct

    @Transactional(readOnly = true)
    public List<ProductInCart> getProducts(String id){
        int ident = Integer.parseInt(id);
        return cartRepository.findByBuyer(ident).getProducts();
    }//getProducts

    @Transactional(readOnly = true)
    public List<ProductInPromoInCart> getProductsInPromo(String id){
        int ident = Integer.parseInt(id);
        return cartRepository.findByBuyer(ident).getProductsInPromo();
    }//getProductsInPromo

    @Transactional(readOnly = true)
    public Cart getCart(String id){
        int ident = Integer.parseInt(id);
        return cartRepository.findByBuyer(ident);
    }//getCart

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public Cart updateProductQuantity(String id, int idP, int quantity) throws ProductInCartNotFoundException {
        int ident = Integer.parseInt(id);
        if(productInCartRepository.existsById(idP)){
            Cart cart = cartRepository.findByBuyer(ident);
            if(cart.getProducts().contains(productInCartRepository.getById(idP))){
                productInCartRepository.getById(idP).setQuantity(productInCartRepository.getById(idP).getQuantity()+quantity);
                return cart;
            } else {
                throw new ProductInCartNotFoundException();
            }
        } else {
            throw new ProductInCartNotFoundException();
        }
    }//updateProductQuantity

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public Cart updateProductInPromoQuantity(String id, int idPip, int quantity) throws ProductInPromoInCartNotFoundException {
        int ident = Integer.parseInt(id);
        if(productInPromoInCartRepository.existsById(idPip)){
            Cart cart = cartRepository.findByBuyer(ident);
            if(cart.getProducts().contains(productInPromoInCartRepository.getById(idPip))){
                productInPromoInCartRepository.getById(idPip).setQuantity(productInPromoInCartRepository.getById(idPip).getQuantity()+quantity);
                return cart;
            } else {
                throw new ProductInPromoInCartNotFoundException();
            }
        } else {
            throw new ProductInPromoInCartNotFoundException();
        }
    }//updateProductQuantity

}//CartService
