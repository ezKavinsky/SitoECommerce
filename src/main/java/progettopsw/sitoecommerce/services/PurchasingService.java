package progettopsw.sitoecommerce.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import progettopsw.sitoecommerce.repositories.PurchaseRepository;
import progettopsw.sitoecommerce.repositories.UserRepository;

@Service
public class PurchasingService {
    @Autowired
    private PurchaseRepository purchaseRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductsInPurchaseRepository




}//PurchasingService
