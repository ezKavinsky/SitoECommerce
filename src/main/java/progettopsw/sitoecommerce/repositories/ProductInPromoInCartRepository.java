package progettopsw.sitoecommerce.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import progettopsw.sitoecommerce.entities.Cart;
import progettopsw.sitoecommerce.entities.ProductInPromo;
import progettopsw.sitoecommerce.entities.ProductInPromoInCart;

@Repository
public interface ProductInPromoInCartRepository extends JpaRepository<ProductInPromoInCart, Integer> {

    boolean existsByProductInPromo(ProductInPromo pip);
    boolean existsByProductInPromoAndCart(ProductInPromo pip, Cart cart);

    @Query("select p from ProductInPromoInCart p where p.productInPromo = ?1 and p.cart = ?2")
    ProductInPromoInCart findByProductInPromoAndCart(ProductInPromo pip, Cart cart);
}//ProductInPromoInCart
