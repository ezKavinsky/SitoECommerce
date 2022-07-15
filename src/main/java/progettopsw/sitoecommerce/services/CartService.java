package progettopsw.sitoecommerce.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import progettopsw.sitoecommerce.entities.*;
import progettopsw.sitoecommerce.repositories.*;
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
    public void delete(int id){
        cartRepository.delete(cartRepository.findByBuyer(id));
    }//delete

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public Cart addProduct(Product product,int quantity, String id) throws ProductNotFoundException{
        if(productRepository.existsById(product.getId())){
            int ident = Integer.parseInt(id);
            ProductInCart pic = new ProductInCart();
            pic.setCart(cartRepository.findByBuyer(ident));
            pic.setQuantity(quantity);
            pic.setProduct(product);
            ProductInCart justAdded = productInCartRepository.save(pic);
            Cart result = cartRepository.findByBuyer(ident);
            result.getProducts().add(justAdded);
            productRepository.getById(product.getId()).getProductsInCarts().add(justAdded);
            return result;
        } else {
            throw new ProductNotFoundException();
        }
    }//addProduct

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public Cart addProductInPromo(ProductInPromo productInPromo, int quantity, String id) throws ProductInPromoNotFoundException {
        if(productInPromoRepository.existsById(productInPromo.getId())){
            int ident = Integer.parseInt(id);
            ProductInPromoInCart pipic = new ProductInPromoInCart();
            pipic.setCart(cartRepository.findByBuyer(ident));
            pipic.setQuantity(quantity);
            pipic.setProductInPromo(productInPromo);
            ProductInPromoInCart justAdded = productInPromoInCartRepository.save(pipic);
            Cart result = cartRepository.findByBuyer(ident);
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

}//CartService
