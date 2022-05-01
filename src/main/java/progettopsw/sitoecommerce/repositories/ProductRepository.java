package progettopsw.sitoecommerce.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import progettopsw.sitoecommerce.entities.Product;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    List<Product> findByNameContaining(String name); //trova per nome che contiene la stringa passata come parametro
    List<Product> findByBarCode(String name);
    boolean existsByBarCode(String barCode);

    @Query("select p from Product p where (p.name like ?1 or ?1 is null) and (p.quantity > ?2 or ?2 is null) and (p.price > ?3 or ?3 is null)")
    List<Product> advancedSearch(String name, int quantity, int price);


}//ProductRepository
