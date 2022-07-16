package progettopsw.sitoecommerce.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import progettopsw.sitoecommerce.entities.Cart;
import progettopsw.sitoecommerce.entities.Product;
import progettopsw.sitoecommerce.entities.ProductInCart;

@Repository
public interface ProductInCartRepository extends JpaRepository<ProductInCart, Integer> {

    boolean existsByProduct(int id);
    boolean existsByProductAndCart(Product product, Cart cart);

    @Query("select p from ProductInCart p where p.product = ?1 and p.cart = ?2")
    ProductInCart findByProductAndCart(Product product, Cart cart);

}//ProductInCartRepository
