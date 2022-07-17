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
        Cart cart = cartRepository.getById(idC);
        Product product = productRepository.getById(ident);
        if(productInCartRepository.existsByProductAndCart(product, cart)){
            ProductInCart productInCart = productInCartRepository.findByProductAndCart(product, cart);
            productInCart.setQuantity(productInCart.getQuantity() + quantity);
            cart.setTotal(cart.getTotal()+ product.getPrice()*quantity);
            return cart;
        }
        else if(productRepository.existsById(ident)){
            ProductInCart pic = new ProductInCart();
            pic.setCart(cart);
            pic.setQuantity(quantity);
            pic.setProduct(product);
            ProductInCart justAdded = productInCartRepository.save(pic);
            cart.getProducts().add(justAdded);
            productRepository.getById(product.getId()).getProductsInCarts().add(justAdded);
            cart.setTotal(cart.getTotal()+ product.getPrice()*quantity);
            return cart;
        } else {
            throw new ProductNotFoundException();
        }
    }//addProduct

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public Cart addProductInPromo(String id, int idC, int quantity) throws ProductInPromoNotFoundException {
        int ident = Integer.parseInt(id);
        Cart cart = cartRepository.getById(idC);
        ProductInPromo pip = productInPromoRepository.getById(ident);
        if(productInPromoInCartRepository.existsByProductInPromoAndCart(pip, cart)){
            ProductInPromoInCart productInPromoInCart = productInPromoInCartRepository.findByProductInPromoAndCart(pip, cart);
            productInPromoInCart.setQuantity(productInPromoInCart.getQuantity() + quantity);
            cart.setTotal(cart.getTotal()+ pip.getDiscountPrice()*quantity);
            return cart;
        }
        else if(productInPromoRepository.existsById(ident)){
            ProductInPromo productInPromo = productInPromoRepository.getById(ident);
            ProductInPromoInCart pipic = new ProductInPromoInCart();
            pipic.setCart(cartRepository.getById(idC));
            pipic.setQuantity(quantity);
            pipic.setProductInPromo(productInPromo);
            ProductInPromoInCart justAdded = productInPromoInCartRepository.save(pipic);
            Cart result = cartRepository.getById(idC);
            result.getProductsInPromo().add(justAdded);
            productInPromoRepository.getById(productInPromo.getId()).getProductsInPromoInCarts().add(justAdded);
            cart.setTotal(cart.getTotal()+ pip.getDiscountPrice()*quantity);
            return cart;
        } else {
            throw new ProductInPromoNotFoundException();
        }
    }//addProductInPromo

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void removeProduct(String id1, String id2){
        int ident1 = Integer.parseInt(id1);
        int ident2 = Integer.parseInt(id2);
        ProductInCart productInCart = productInCartRepository.getById(ident2);
        Cart cart = cartRepository.findByBuyer(ident1);
        cart.setTotal(cart.getTotal()-(productInCart.getQuantity()*productInCart.getProduct().getPrice()));
        cart.getProducts().remove(productInCart);
        productRepository.getById(productInCart.getProduct().getId()).getProductsInCarts().remove(productInCart);
        productInCartRepository.delete(productInCart);
    }//removeProduct

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void removeProductInPromo(String id1, String id2){
        int ident1 = Integer.parseInt(id1);
        int ident2 = Integer.parseInt(id2);
        ProductInPromoInCart productInPromoInCart = productInPromoInCartRepository.getById(ident2);
        Cart cart = cartRepository.findByBuyer(ident1);
        cart.setTotal(cart.getTotal()-(productInPromoInCart.getQuantity()*productInPromoInCart.getProductInPromo().getDiscountPrice()));
        cart.getProductsInPromo().remove(productInPromoInCart);
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
        Product p = productRepository.getById(idP);
        Cart cart = cartRepository.findByBuyer(ident);
        if(productInCartRepository.existsByProductAndCart(p, cart)){
            if(cart.getProducts().contains(productInCartRepository.findByProductAndCart(p, cart))){
                productInCartRepository.findByProductAndCart(p, cart)
                        .setQuantity(productInCartRepository.findByProductAndCart(p, cart).getQuantity()+quantity);
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
        ProductInPromo pip = productInPromoRepository.getById(idPip);
        Cart cart = cartRepository.findByBuyer(ident);
        if(productInPromoInCartRepository.existsByProductInPromoAndCart(pip, cart)){
            if(cart.getProductsInPromo().contains(productInPromoInCartRepository.findByProductInPromoAndCart(pip, cart))){
                productInPromoInCartRepository.findByProductInPromoAndCart(pip, cart)
                        .setQuantity(productInPromoInCartRepository.findByProductInPromoAndCart(pip, cart).getQuantity()+quantity);
                return cart;
            } else {
                throw new ProductInPromoInCartNotFoundException();
            }
        } else {
            throw new ProductInPromoInCartNotFoundException();
        }
    }//updateProductQuantity

}//CartService
