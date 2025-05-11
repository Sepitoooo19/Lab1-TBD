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
import java.util.Map;
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

    @Transactional(readOnly = true)
    public List<Map<String, Object>> getTopProductsByCategoryForLastMonth() {
        return repo.findTopProductsByCategoryForLastMonth();
    }

    //findByCompanyId en repository
    @Transactional(readOnly = true)
    public List<ProductEntity> getProductsByCompanyId(int companyId) {
        return repo.findByCompanyId(companyId);
    }

    /*
    * //GetCompanyIdByProductId
    public int getCompanyIdByProductId(int productId) {
        String sql = "SELECT company_id FROM products WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{productId}, Integer.class);
    }*/

    //GetCompanyIdByProductId
    @Transactional(readOnly = true)
    public int getCompanyIdByProductId(int productId) {
        return repo.getCompanyIdByProductId(productId);
    }





}
