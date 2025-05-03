package bdavanzadas.lab1.services;

import bdavanzadas.lab1.entities.OrdersEntity;
import bdavanzadas.lab1.entities.ProductEntity;
import bdavanzadas.lab1.repositories.OrdersRepository;
import bdavanzadas.lab1.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {
    @Autowired
    private ProductRepository repo;
    @Autowired
    private OrdersRepository ordersRepo;

    @Transactional(readOnly = true)
    public List<ProductEntity> getAllProducts() {
        return repo.findAll();
    }
    @Transactional(readOnly = true)
    public ProductEntity getProductById(int id) {
        return repo.findbyid(id);
    }
    @Transactional
    public void saveProduct(ProductEntity product) {
        repo.save(product);
    }
    @Transactional
    public void updateProduct(ProductEntity product) {
        repo.update(product);
    }
    @Transactional
    public void deleteProduct(int id) {
        repo.delete(id);
    }
    @Transactional(readOnly = true)
    public List<ProductEntity> getProductsByStock() {
        return repo.findByStock();
    }
    @Transactional
    public List<ProductEntity> getProductsByCategory(String category) {return repo.findbyCategory(category);}
    @Transactional
    public List<String> findallCategories(){
        return repo.findAllCategories();
    }
    //RF 02: obtener los productos con mas pedidos en el mes segun categoria
    @Transactional
    public List<List<ProductEntity>> getTopProductsByCategory() {
        List<String> categories = repo.findAllCategories();
        List<List<ProductEntity>> result = new ArrayList<>();

        // Obtener la fecha actual
        LocalDate currentDate = LocalDate.now();
        int currentMonth = currentDate.getMonthValue();
        int currentYear = currentDate.getYear();

        // Iterar por cada categoría
        for (String category : categories) {
            // Obtener los productos más pedidos por categoría en el mes actual
            List<ProductEntity> topProducts = repo.getTopProductsByCategoryAndMonth(category, currentMonth, currentYear);
            result.add(topProducts);
        }

        return result;
    }

}
