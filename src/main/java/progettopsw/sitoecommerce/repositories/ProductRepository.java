package progettopsw.sitoecommerce.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import progettopsw.sitoecommerce.entities.Product;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    List<Product> findByNameContaining(String name); //trova per nome che contiene la stringa passata come parametro
    List<Product> findByBarCode(String name);
    boolean existsByBarCode(String barCode);


}//ProductRepository
