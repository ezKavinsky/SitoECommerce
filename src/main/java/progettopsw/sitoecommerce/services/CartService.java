package progettopsw.sitoecommerce.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import progettopsw.sitoecommerce.entities.Cart;
import progettopsw.sitoecommerce.entities.Product;
import progettopsw.sitoecommerce.entities.ProductInPromo;
import progettopsw.sitoecommerce.entities.User;
import progettopsw.sitoecommerce.repositories.CartRepository;
import progettopsw.sitoecommerce.repositories.ProductInPromoRepository;
import progettopsw.sitoecommerce.repositories.ProductRepository;
import progettopsw.sitoecommerce.repositories.UserRepository;
import progettopsw.sitoecommerce.support.exceptions.ProductInPromoNotFoundException;
import progettopsw.sitoecommerce.support.exceptions.ProductNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public Cart create(int id) {
        Cart cart = new Cart();
        cart.setBuyer(userRepository.getById(id));
        userRepository.getById(id).setCart(cart);
        return cartRepository.save(cart);
    }//create

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void delete(int id){
        cartRepository.delete(cartRepository.findByBuyer(id));
    }//delete

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public Cart addProduct(Product product,String id) throws ProductNotFoundException{
        if(productRepository.existsById(product.getId())){
            int ident = Integer.parseInt(id);
            Cart result = cartRepository.findByBuyer(ident);
            result.getProducts().add(product);
            productRepository.getById(product.getId()).setCart(result);
            return result;
        } else {
            throw new ProductNotFoundException();
        }
    }//addProduct

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public Cart addProductInPromo(ProductInPromo productInPromo, String id) throws ProductInPromoNotFoundException {
        if(productInPromoRepository.existsById(productInPromo.getId())){
            int ident = Integer.parseInt(id);
            Cart result = cartRepository.findByBuyer(ident);
            result.getProductsInPromo().add(productInPromo);
            productInPromoRepository.getById(productInPromo.getId()).setCart(result);
            return result;
        } else {
            throw new ProductInPromoNotFoundException();
        }
    }//addProductInPromo

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void removeProduct(String id, Product product){
        int ident = Integer.parseInt(id);
        cartRepository.findByBuyer(ident).getProducts().remove(product);
        productRepository.getById(product.getId()).setCart(null);
    }//removeProduct

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void removeProductInPromo(String id, ProductInPromo productInPromo){
        int ident = Integer.parseInt(id);
        cartRepository.findByBuyer(ident).getProductsInPromo().remove(productInPromo);
        productRepository.getById(productInPromo.getId()).setCart(null);
    }//removeProduct

    @Transactional(readOnly = true)
    public List<Product> getProducts(String id){
        int ident = Integer.parseInt(id);
        return cartRepository.findByBuyer(ident).getProducts();
    }//getProducts

    @Transactional(readOnly = true)
    public List<ProductInPromo> getProductsInPromo(String id){
        int ident = Integer.parseInt(id);
        return cartRepository.findByBuyer(ident).getProductsInPromo();
    }//getProductsInPromo

    @Transactional(readOnly = true)
    public Cart getCart(String id){
        int ident = Integer.parseInt(id);
        return cartRepository.findByBuyer(ident);
    }//getCart

}//CartService
